package com.datatrees.gongfudai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.widget.CustomWebView;

import java.util.Arrays;
import java.util.Collections;

import butterknife.Bind;

/**
 * Created by ucmed on 2015/7/31.
 */
public class WebClientActivity extends BaseActivity implements CustomWebView.OnVisitEndUrl {
    @Bind(R.id.custom_webview)
    CustomWebView mWebView;

    String url;
    String[] endUrls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_client);
        BK.bind(this);
        Intent extras = getIntent();
        url = extras.getStringExtra("visit_url");
        endUrls = extras.getStringArrayExtra("end_urls");
        mWebView.setOnVisitEndUrl(this);
        mWebView.customLoadUrl(url, endUrls);
    }

    @Override
    public void onVisitEndUrl(String endUrl, String[] cookies) {
        Log.i("TAG", "end cookies size-->" + cookies.length);
        Log.i("TAG", "end cookies-->" + Arrays.toString(cookies));
        Log.i("TAG", "end url-->" + endUrl);
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
}
