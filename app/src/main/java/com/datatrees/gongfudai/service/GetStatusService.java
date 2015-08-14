package com.datatrees.gongfudai.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.net.VolleyUtil;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.PreferenceUtils;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 轮训获取资料手机状态
 * Created by zhangping on 15/8/14.
 */
public class GetStatusService extends Service implements RespListener.OnRespSuccess, RespListener.OnRespError {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean isStart = PreferenceUtils.getPrefBoolean(App.getContext(), "GetStatusThread_IS_START", false);
        if (!isStart) {
            PreferenceUtils.setPrefBoolean(App.getContext(), "GetStatusThread_IS_START", true);
            new GetStatusThread().start();
        }

        return super.onStartCommand(intent, START_STICKY, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static int TIME_INTERVAL = 30 * 1000;

    @Override
    public void onError(String errorResp, String extras) {
        if (StringUtils.isTrimBlank(errorResp))
            return;
        ToastUtils.showShort(errorResp);
    }

    @Override
    public void onSuccess(JSONObject response, String extras) {
        if (response == null || StringUtils.isTrimBlank(response.toString()))
            return;
        try {
            JSONObject allstatusJSON = response.optJSONObject("allstatus");
            App.allstatusMap.put("idcard", allstatusJSON.optJSONObject("idcard"));
            App.allstatusMap.put("operator", allstatusJSON.optJSONObject("operator"));
            App.allstatusMap.put("ecommerce", allstatusJSON.optJSONObject("ecommerce"));
            App.allstatusMap.put("email", allstatusJSON.optJSONObject("email"));
            App.allstatusMap.put("ice", allstatusJSON.optJSONObject("ice"));
            App.allstatusMap.put("contacts", allstatusJSON.optJSONObject("contacts"));
            JSONArray verifyArray = response.optJSONArray("operator");
            for (int i = 0; i < verifyArray.length(); i++) {
                JSONObject obj = verifyArray.optJSONObject(i);
                App.verifyMap.put(obj.optString("key"), obj);
            }
            Intent intent = new Intent();
            intent.setAction(VerifyReciver.VERFY_RECEIVED);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class GetStatusThread extends Thread implements Runnable {
        @Override
        public void run() {
            while (true) {
                LogUtil.i("GetStatusThread time --->" + System.currentTimeMillis());
                if (!App.isInHand) {
                    RespListener respListener = new RespListener();
                    respListener.onRespError = GetStatusService.this;
                    respListener.onRespSuccess = GetStatusService.this;
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId", App.loginUserInfo.getUserId() + "");
                    CustomStringRequest request = new CustomStringRequest(Request.Method.GET, DsApi.GETPRESTATUS, respListener, params);
                    VolleyUtil.addRequest(request);
                    LogUtil.i("GetStatusThread send Request ");
                }
                try {
                    sleep(TIME_INTERVAL);
                } catch (InterruptedException e) {
                }
            }

        }
    }
}
