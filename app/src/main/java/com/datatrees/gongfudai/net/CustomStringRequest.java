/*
 * StringRequest.java
 * @author Allen Zhang
 * V 1.0.0
 * Create at 2014-6-20 下午2:20:59
 */

package com.datatrees.gongfudai.net;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

/**
 * com.hengtian.zdwpoaapp.net.StringRequest
 * 
 * @author Allen Zhang <br/>
 *         create at 2014-6-20 下午2:20:59
 */
public class CustomStringRequest extends StringRequest {

    /**
     * @param method
     * @param url
     * @param listener
     * @param errorListener
     */
    public CustomStringRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
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
