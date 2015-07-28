package com.datatrees.gongfudai;

import android.os.Bundle;
import android.widget.Button;

import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.utils.BK;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhangping on 15/7/25.
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.btn_test)
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BK.bind(this);
        btnTest.setText("ddddd");
    }

    @OnClick(R.id.btn_test)
    public void testOnclick() {

    }
}
