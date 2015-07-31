package com.datatrees.gongfudai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.datatrees.gongfudai.utils.StringUtils;

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

    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * @param url     visit url
     * @param endUrls end urls
     */
    public void customLoadUrl(String url, String[] endUrls) {
        if (cookies != null)
            cookies.clear();
        else
            cookies = new ArrayList<String>();
        this.endUrls = endUrls;
        loadUrl(url);
    }

    private boolean isEnd(String url) {
        boolean isEnd = false;
        if (StringUtils.isNotTrimBlank(url) && endUrls != null && endUrls.length > 0) {
            for (int i = 0; i < endUrls.length; i++) {
                if (endUrls[i].equals(url)) {
                    isEnd = true;
                    break;
                }
            }
        }
        return isEnd;
    }

    private void init() {
        cookies = new ArrayList<String>();
        getSettings().setAppCacheEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("TAG","loadUrl-->"+url);
                if (isEnd(url) && onVisitEndUrl != null)
                    onVisitEndUrl.onVisitEndUrl(url, (String[]) cookies.toArray(new String[cookies.size()]));
                else
                    loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);
                Log.e("TAG", "Cookies = " + cookieStr);
                if (StringUtils.isNotTrimBlank(cookieStr)) {
                    cookies.add(cookieStr);
                }
                super.onPageFinished(view, url);
            }

        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && canGoBack()) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.getCookie(url);
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
//        cookieManager.removeAllCookies(null);
//        cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    public void setOnVisitEndUrl(OnVisitEndUrl onVisitEndUrl) {
        this.onVisitEndUrl = onVisitEndUrl;
    }

    public interface OnVisitEndUrl {
        public void onVisitEndUrl(String endUrl, String[] cookies);
    }
}
