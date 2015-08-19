package com.datatrees.gongfudai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.datatrees.gongfudai.base.BaseFragmentActivity;
import com.datatrees.gongfudai.login.LoginActivity;
import com.datatrees.gongfudai.login.LoginRegisterActivity;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.PreferenceUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * WelcomeActivity
 * Created by zhangping on 15/8/19.
 */
public class WelcomeActivity extends BaseFragmentActivity {

    ImageView welcomeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(this);
        setContentView(R.layout.activity_welcome);
        welcomeImage = (ImageView) findViewById(R.id.welcome_imageview);
        AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f); // 后面2个参数设置透明度
        // 设置动画持续时间 2秒钟
        aa.setDuration(5000);
        // 在图片上应用动画
        welcomeImage.startAnimation(aa);

        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }
        });
    }

    private void startActivity() {
        boolean mFirst = isFirstEnter(WelcomeActivity.this,
                WelcomeActivity.this.getClass().getName());
//        if (mFirst) {
//            startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
//            finish();
//        } else {
        long loginUserId = PreferenceUtils.getPrefLong(this, ConstantUtils.LOGIN_USERID, 0);
        if (loginUserId != 0) {
            startActivity(new Intent(WelcomeActivity.this,
                    LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(WelcomeActivity.this,
                    LoginRegisterActivity.class));
            finish();
        }

//        }
    }


    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null
                || "".equalsIgnoreCase(className))
            return false;
        boolean mResultStr = PreferenceUtils.getPrefBoolean(App.getContext(), ConstantUtils.KEY_GUIDE_ACTIVITY, false);
        if (mResultStr)
            return false;
        else
            return true;
    }
}
