package com.datatrees.gongfudai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.widget.CustomWebView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ucmed on 2015/7/31.
 */
public class WebClientActivity extends BaseActivity implements CustomWebView.OnVisitEndUrl {
    @Bind(R.id.custom_webview)
    CustomWebView mWebView;
    @Bind(R.id.tv_title)
    TextView tv_title;

    String url;
    String[] endUrls;
    String title;
    String cssStr;
    boolean usePCUA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_client);
        BK.bind(this);
        clearCookieHis();

        Intent extras = getIntent();
        cssStr = extras.getStringExtra("insert_css");
        title = extras.getStringExtra("visit_title");
        url = extras.getStringExtra("visit_url");
        usePCUA = extras.getBooleanExtra("usePCUA", false);

        tv_title.setText(title);
        endUrls = extras.getStringArrayExtra("end_urls");
        mWebView.setOnVisitEndUrl(this);

        mWebView.customLoadUrl(url, endUrls, cssStr, usePCUA);
    }

    private void clearCookieHis() {
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        mWebView.clearCache(true);
        mWebView.clearHistory();
    }

    @Override
    public void onVisitEndUrl(String endUrl, String[] cookies, String headerStr) {
        LogUtil.i("TAG", "end cookies size-->" + cookies.length);
        LogUtil.i("TAG", "end cookies-->" + Arrays.toString(cookies));
        LogUtil.i("TAG", "end url-->" + endUrl);
        LogUtil.i("TAG", "end header-->" + headerStr);

        clearCookieHis();

        Intent data = new Intent();
        data.putExtra("end_cookies", cookies);
        data.putExtra("end_url", endUrl);
        data.putExtra("end_header", StringUtils.isTrimBlank(headerStr) ? "" : headerStr);
        setResult(RESULT_OK, data);
        this.finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.ibtn_back)
    public void goBack() {
        this.finish();
    }

}
