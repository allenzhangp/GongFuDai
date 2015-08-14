package com.datatrees.gongfudai.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.StringUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * use customLoadUrl(url,endUrls) when visit endUrls will callback onVisitEndUrl.
 * Created by zhangping on 15/7/25.
 */
public class CustomWebView extends WebView {

    private String[] endUrls;
    private List<String> cookies;
    private OnVisitEndUrl onVisitEndUrl;
    private String headerJSONObj;
    private String cssStr;
    private StringBuilder jsCssStr;

    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * @param url     visit url
     * @param endUrls end urls
     */
    public void customLoadUrl(String url, String[] endUrls, String cssStr) {
        if (cookies != null)
            cookies.clear();
        else
            cookies = new ArrayList<>();
        this.endUrls = endUrls;
        this.cssStr = cssStr;

        if (StringUtils.isNotTrimBlank(cssStr)) {
            jsCssStr = new StringBuilder();
            jsCssStr.append("var newscript = document.createElement(\"script\");");
            jsCssStr.append("newscript.onload=function(){");
            jsCssStr.append("var newstyle = document.createElement(\"style\");");
            jsCssStr.append("newstyle.type=\"text/css\"");
            jsCssStr.append("newstyle.appendChild(document.createTextNode(");
            jsCssStr.append(cssStr);
            jsCssStr.append("))");
            jsCssStr.append("};");
        }

        loadUrl(url);
    }

    private boolean isEnd(String url) {
        boolean isEnd = false;
        if (StringUtils.isNotTrimBlank(url) && endUrls != null && endUrls.length > 0) {
            for (int i = 0; i < endUrls.length; i++) {
                if (url.contains(endUrls[i])) {
                    isEnd = true;
                    break;
                }
            }
        }
        return isEnd;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String url = (String) msg.obj;
            if (isEnd(url) && onVisitEndUrl != null) {
                LogUtil.i("TAG", "end url-->" + url);
                onVisitEndUrl.onVisitEndUrl(url, (String[]) cookies.toArray(new String[cookies.size()]), headerJSONObj);
            } else {
                loadUrl(url);
            }
        }
    };

    private void init() {
        cookies = new ArrayList<>();
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDefaultTextEncodingName("utf-8");
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                LogUtil.d("info", "===>>> shouldOverrideUrlLoading method is called!");
                Thread theard = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final HttpGet httpGet = new HttpGet(url);
                            HttpResponse response;
                            HttpClient httpClient = new DefaultHttpClient();
                            response = httpClient.execute(httpGet);
                            JSONObject jsonObject = null;
                            if (response.getStatusLine().getStatusCode() == 200) {
                                Header[] headers = response.getAllHeaders();
                                for (Header header : headers) {
                                    String name = header.getName();
                                    String value = header.getValue();
                                    LogUtil.d("info", "===>>> name:" + name);
                                    LogUtil.d("info", "===>>> value:" + value);
                                    jsonObject = new JSONObject();
                                    jsonObject.put(name, value);

                                }
                                if (jsonObject != null)
                                    headerJSONObj = jsonObject.toString();

                                Message msg = handler.obtainMessage();
                                msg.obj = url;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                theard.start();


                return true;
            }

            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);
                LogUtil.e("TAG", "Cookies = " + cookieStr);
                if (StringUtils.isNotTrimBlank(cookieStr)) {
                    cookies.add(cookieStr);
                }
                if (jsCssStr != null && StringUtils.isNotTrimBlank(jsCssStr.toString()))
                    loadUrl("javascript:" + jsCssStr.toString());
                super.onPageFinished(view, url);
            }

        });
    }

    public String convertToString(InputStream inputStream) {
        StringBuffer string = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                string.append(line + "\n");
            }
        } catch (IOException e) {
        }
        return string.toString();
    }

    public void setOnVisitEndUrl(OnVisitEndUrl onVisitEndUrl) {
        this.onVisitEndUrl = onVisitEndUrl;
    }

    public interface OnVisitEndUrl {
        void onVisitEndUrl(String endUrl, String[] cookies, String headerStr);
    }

}
