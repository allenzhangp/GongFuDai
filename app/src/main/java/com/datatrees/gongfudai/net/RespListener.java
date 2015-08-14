package com.datatrees.gongfudai.net;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
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
            String errorMsg = obj.optString("errorMsg");
            int code = obj.optInt("code");
            long timestamp = obj.optLong("timestamp");
            App.timestamp = timestamp;
            if (onRespSuccess != null)
                onRespSuccess.onSuccess(obj.optJSONObject("data"), extras);
        } catch (JSONException e) {
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
                    onRespError.onError(jsonObject.optString("errorMsg"), extras);
                } else
                    onRespError.onError(App.getContext().getResources().getString(R.string.server_case_error), extras);
            }

        } catch (Exception e) {
            onRespError.onError(App.getContext().getResources().getString(R.string.server_case_error), extras);
        }
    }


    public interface OnRespSuccess {
        public void onSuccess(JSONObject response, String extras);
    }

    public interface OnRespError {
        public void onError(String errorResp, String extras);
    }
}
