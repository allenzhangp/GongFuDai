package com.datatrees.gongfudai;

import android.app.Application;

/**
 * Created by zhangping on 15/7/25.
 */
public class App extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyUtil.init(this);
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
