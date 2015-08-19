/*
 * StringRequest.java
 * @author Allen Zhang
 * V 1.0.0
 * Create at 2014-6-20 下午2:20:59
 */

package com.datatrees.gongfudai.net;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.volley.AuthFailureError;
import com.datatrees.gongfudai.volley.DefaultRetryPolicy;
import com.datatrees.gongfudai.volley.NetworkResponse;
import com.datatrees.gongfudai.volley.Response;
import com.datatrees.gongfudai.volley.RetryPolicy;
import com.datatrees.gongfudai.volley.toolbox.HttpHeaderParser;
import com.datatrees.gongfudai.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author Allen Zhang <br/>
 *         create at 2014-6-20 下午2:20:59
 */
public class CustomStringRequest extends StringRequest {

    private Map<String, String> mMap;
    private RespListener listener = new RespListener();

    public CustomStringRequest(int method, String url, RespListener listener) {
        super(method, url, listener, listener);
        LogUtil.i(url);
    }


    public CustomStringRequest(int method, String url, RespListener listener, Map<String, String> params) {
        super(method, url, listener, listener);
        this.mMap = params;
        if (App.loginUserInfo != null) {
            this.mMap.put("token", App.loginUserInfo.getToken());
        }
        LogUtil.i("url:" + url);
        StringBuilder sb = new StringBuilder("{");
        for (String key : params.keySet()) {
            sb.append("\"" + key + "\":\"" + params.get(key) + "\",");
        }
        sb.append("}");
        LogUtil.i("params:" + sb.toString());

    }

    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(ConstantUtils.CUSTOM_TIMEOUT_MS, ConstantUtils.CUSTOM_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String str = null;
        try {
            str = new String(response.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
    }
}
