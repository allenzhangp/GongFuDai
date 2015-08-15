package com.datatrees.gongfudai.login;

import android.content.Intent;
import android.os.Bundle;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.utils.BK;

import butterknife.OnClick;

/**
 * LoginRegister
 * Created by zhangping on 15/8/14.
 */
public class LoginRegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        BK.bind(this);
    }

    @OnClick(R.id.btn_register)
    public void toRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        App.addActivity(this);

    }

    @OnClick(R.id.btn_submit)
    public void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        App.addActivity(this);
    }

}
