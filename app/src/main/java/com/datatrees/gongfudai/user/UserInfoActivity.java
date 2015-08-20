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
 * UserInfoActivity
 * Created by zhangping on 15/8/20.
 */
public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView titleView;

    @Bind(R.id.tv_user_dszh)
    TextView tv_user_dszh;
    @Bind(R.id.tv_user_email)
    TextView tv_user_email;
    @Bind(R.id.tv_user_idcard)
    TextView tv_user_idcard;
    @Bind(R.id.tv_user_jjlxr)
    TextView tv_user_jjlxr;
    @Bind(R.id.tv_user_name)
    TextView tv_user_name;
    @Bind(R.id.tv_user_phone)
    TextView tv_user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        BK.bind(this);
        init();
    }

    private void init() {
        titleView.setText(R.string.user_info);
        tv_user_phone.setText(PreferenceUtils.getPrefString(App.getContext(), ConstantUtils.LOGIN_NAME, ""));

    }

    @OnClick(R.id.ibtn_back)
    public void goBack() {
        this.finish();
    }
}
