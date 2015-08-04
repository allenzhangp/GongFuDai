package com.datatrees.gongfudai.net;

import com.datatrees.gongfudai.volley.AuthFailureError;
import com.datatrees.gongfudai.volley.NetworkResponse;
import com.datatrees.gongfudai.volley.Request;
import com.datatrees.gongfudai.volley.Response;
import com.datatrees.gongfudai.volley.VolleyLog;


import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by ucmed on 2015/8/4.
 */
public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private static final String FILE_PART_NAME = "file";
    private final HashMap<String, String> params;

    private final Response.Listener<String> mListener;
    private final File mFilePart;

    public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, File file, HashMap<String, String> params)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mFilePart = file;
        this.params = params;

        buildMultipartEntity();
    }

    private void buildMultipartEntity()
    {
        entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
        try
        {
            for ( String key : params.keySet() ) {
                entity.addPart(key, new StringBody(params.get(key)));
            }
        }
        catch (UnsupportedEncodingException e)
        {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType()
    {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            entity.writeTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        return Response.success("Uploaded", getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }
}