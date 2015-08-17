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
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;


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
        isFinish = false;
        new GetStatusThread().start();

        return super.onStartCommand(intent, START_STICKY, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static int TIME_INTERVAL = 30 * 1000;
    public static boolean isFinish = false;

    @Override
    public void onError(String errorResp, String extras) {
        if (StringUtils.isTrimBlank(errorResp))
            return;
        ToastUtils.showShort(errorResp);
    }

    @Override
    public void onSuccess(String response, String extras) {
        if (response == null || StringUtils.isTrimBlank(response.toString()))
            return;
        try {
            JSONObject jsonResp = new JSONObject(response);
            JSONObject allstatusJSON = jsonResp.optJSONObject("allstatus");
            App.allstatusMap.put("idcard", allstatusJSON.optJSONObject("idcard"));
            App.allstatusMap.put("operator", allstatusJSON.optJSONObject("operator"));
            App.allstatusMap.put("ecommerce", allstatusJSON.optJSONObject("ecommerce"));
            App.allstatusMap.put("email", allstatusJSON.optJSONObject("email"));
            App.allstatusMap.put("ice", allstatusJSON.optJSONObject("ice"));
            App.allstatusMap.put("contacts", allstatusJSON.optJSONObject("contacts"));
            JSONArray verifyArray = jsonResp.optJSONArray("operate");
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
            while (!isFinish) {
                if (!App.isInHand) {
                    RespListener respListener = new RespListener();
                    respListener.onRespError = GetStatusService.this;
                    respListener.onRespSuccess = GetStatusService.this;
                    CustomStringRequest request = new CustomStringRequest(Request.Method.GET, DsApi.getTokenUserId(String.format(DsApi.LIST, DsApi.GETPRESTATUS)), respListener);
                    VolleyUtil.addRequest(request);
                    LogUtil.i("GetStatusThread send Request ");
                }
                try {
                    sleep(TIME_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
