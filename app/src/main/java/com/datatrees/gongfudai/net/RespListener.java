package com.datatrees.gongfudai.net;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.volley.Response;
import com.datatrees.gongfudai.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * custom resp
 * Created by zhangping on 15/8/12.
 */
public class RespListener implements Response.Listener<String>, Response.ErrorListener {

    public OnRespSuccess onRespSuccess;
    public OnRespError onRespError;

    private String extras = null;

    public RespListener() {
    }

    public RespListener(String extras) {
        this.extras = extras;
    }

    @Override
    public void onResponse(String response) {
        try {
            LogUtil.i(response);
            JSONObject obj = new JSONObject(response);
            if (obj.has("msg") || obj.has("object")) {
                int code = obj.optInt("code");
                if (code == 0) {
                    if (onRespSuccess != null)
                        onRespSuccess.onSuccess(obj.optString("object"), extras);
                } else {
                    if (onRespError != null)
                        onRespError.onError(obj.optString("msg"), extras);
                }
            } else {
                if (onRespSuccess != null)
                    onRespSuccess.onSuccess(obj.optString("data"), extras);
            }
            long timestamp = obj.has("timestamp") ? obj.optLong("timestamp") : obj.optLong("time");
            App.timestamp = timestamp;
        } catch (JSONException e) {
            if (onRespError != null)
                onRespError.onError(e.getMessage(), extras);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            if (onRespError != null && error.networkResponse != null) {
                String errorResp = new String(error.networkResponse.data);
                if (StringUtils.isNotTrimBlank(errorResp)) {
                    LogUtil.e(errorResp);
                    JSONObject jsonObject = new JSONObject(errorResp);
                    if (jsonObject.has("errorMsg")) {
                        onRespError.onError(jsonObject.optString("errorMsg"), extras);
                    } else {
                        onRespError.onError(error.getMessage(), extras);
                    }
                } else
                    onRespError.onError(error.getMessage(), extras);
            }

        } catch (Exception e) {
            onRespError.onError(e.getMessage(), extras);
        }
    }


    public interface OnRespSuccess {
        public void onSuccess(String response, String extras);
    }

    public interface OnRespError {
        public void onError(String errorResp, String extras);
    }
}
