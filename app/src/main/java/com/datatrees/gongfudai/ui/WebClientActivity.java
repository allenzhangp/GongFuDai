package com.datatrees.gongfudai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.widget.CustomWebView;

import java.util.Collections;

import butterknife.Bind;

/**
 * Created by ucmed on 2015/7/31.
 */
public class WebClientActivity extends BaseActivity {
    @Bind(R.id.custom_webview)
    CustomWebView mWebView;

    String url;
    String[] endUrls;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_web_client);
        Intent extras = getIntent();
        url = extras.getStringExtra("visit_url");
        endUrls = extras.getStringArrayExtra("end_urls");
        mWebView.setOnVisitEndUrl(new CustomWebView.OnVisitEndUrl(){

            @Override
            public void onVisitEndUrl(String endUrl, String[] cookies) {
                Log.i("TAG", "cookies-->"+ cookies.toString());
            }
        });
//        mWebView.customLoadUrl(url, endUrls);
        mWebView.loadUrl(url);
    }
}
