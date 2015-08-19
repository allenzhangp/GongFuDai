package com.datatrees.gongfudai.cordova;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.datatrees.gongfudai.R;

import org.apache.cordova.CordovaWebView;

/**
 * CordovaActivity
 * Created by zhangping on 15/8/18.
 */
public class CordovaActivity extends org.apache.cordova.CordovaActivity implements View.OnClickListener {

    ImageButton ibtn_back;
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cordova);
        url = getIntent().getStringExtra("load_url");
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(this);
        appView = (CordovaWebView) findViewById(R.id.tutorialView);
        appView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                this.finish();
                break;
        }
    }
}
