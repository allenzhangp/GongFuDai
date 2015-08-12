package com.datatrees.gongfudai;

import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.datatrees.gongfudai.model.LoginUserInfo;
import com.datatrees.gongfudai.net.VolleyUtil;

/**
 * Created by zhangping on 15/7/25.
 */
public class App extends Application {
    private static Context mContext;

    public static String AK = "AK";
    public static String SK = "SK";
    public static String SECURITYTOKEN = "securityToken";
    public static String EXPIRATION = "expiration";

    public static final String srcFileDir = "<your data directory>" + "/"; // 该目录下要有两个文件：file1m  file10m

    public static final String BUCKETNAME = "gongfu2";// 指明您的bucket是放在青岛数据中心

    public static OSSService ossService = OSSServiceProvider.getService();

    public static LoginUserInfo loginUserInfo;

    public static long timestamp = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        VolleyUtil.init(this);
        loginUserInfo = new LoginUserInfo();
        mContext = getApplicationContext();
        initOSS();
    }

    private void initOSS() {
        // 打开调试log
        OSSLog.enableLog();

        // 初始化设置
        ossService.setApplicationContext(mContext);
        ossService.setGlobalDefaultHostId(BUCKETNAME); // 设置region host 即 endpoint
        ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN);

    }

    public static Context getContext() {
        return mContext;
    }
}
