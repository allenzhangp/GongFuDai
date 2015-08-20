package com.datatrees.gongfudai.user;

import android.os.Bundle;
import android.widget.TextView;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.PreferenceUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置界面
 * Created by zhangping on 15/8/20.
 */
public class UserSettingActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView titleView;

    @Bind(R.id.tv_user_setting_phone)
    TextView tv_user_setting_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        BK.bind(this);
        init();
    }

    private void init() {
        titleView.setText(R.string.user_setting);
        tv_user_setting_phone.setText(PreferenceUtils.getPrefString(App.getContext(), ConstantUtils.LOGIN_NAME, ""));

    }

    @OnClick(R.id.ibtn_back)
    public void goBack() {
        this.finish();
    }
}
