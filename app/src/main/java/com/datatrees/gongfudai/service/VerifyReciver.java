package com.datatrees.gongfudai.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.HomeActivity;
import com.datatrees.gongfudai.information.InfoSupplementaryActivity;

import org.json.JSONObject;

/**
 * receiver
 * Created by zhangping on 15/8/14.
 */
public class VerifyReciver extends BroadcastReceiver {

    public static String VERFY_RECEIVED = "com.datatrees.gongfudai.verfy";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(VERFY_RECEIVED)) {
            if (context instanceof HomeActivity) {
                boolean statusOk = true;
                HomeActivity homeActivity = (HomeActivity) context;
                for (String keyStr : App.allstatusMap.keySet()) {
                    JSONObject valueObj = App.allstatusMap.get(keyStr);
                    if (valueObj != null) {
                        int status = valueObj.optInt("status");
                        if (status == 3 || status == 4) {

                            statusOk = false;
                            homeActivity.confirm(valueObj.optString("msg"), keyStr);
                            break;
                        }
                    }
                }
                if (statusOk) {
                    for (String keyStr : App.verifyMap.keySet()) {
                        JSONObject valueObj = App.verifyMap.get(keyStr);
                        if (valueObj != null) {

                            if (valueObj.optString("codeType").equals("image"))
                                homeActivity.inputDialog(valueObj.optString("tip"), valueObj.optString("codeContent"), valueObj.optString("key"));
                            else
                                homeActivity.inputDialog(valueObj.optString("tip"), null, valueObj.optString("key"));

                            break;
                        }
                    }
                }
            } else if (context instanceof InfoSupplementaryActivity) {
                boolean statusOk = true;
                InfoSupplementaryActivity infoSupplementaryActivity = (InfoSupplementaryActivity) context;
                for (String keyStr : App.allstatusMap.keySet()) {
                    JSONObject valueObj = App.allstatusMap.get(keyStr);
                    if (valueObj != null) {
                        int status = valueObj.optInt("status");
                        if (status == 3 || status == 4) {
                            statusOk = false;
                            infoSupplementaryActivity.confirm(valueObj.optString("msg"), keyStr);
                            break;
                        }
                    }
                }
                if (statusOk) {
                    for (String keyStr : App.verifyMap.keySet()) {
                        JSONObject valueObj = App.verifyMap.get(keyStr);
                        if (valueObj != null) {
                            if (valueObj.optString("codeType").equals("image"))
                                infoSupplementaryActivity.inputDialog(valueObj.optString("tip"), valueObj.optString("codeContent"), valueObj.optString("key"));
                            else {
                                infoSupplementaryActivity.inputDialog(valueObj.optString("tip"), null, valueObj.optString("key"));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

}