package com.datatrees.gongfudai.base;

import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by ucmed on 2015/8/5.
 */
public class BaseFragmentActivity extends FragmentActivity {
    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.openActivityDurationTrack(false);
            MobclickAgent.onResume(this);       //统计时长
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        super.onPause();
        try {
            MobclickAgent.onPause(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
