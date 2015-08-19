package com.datatrees.gongfudai;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.datatrees.gongfudai.model.LoginUserInfo;
import com.datatrees.gongfudai.net.VolleyUtil;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.PreferenceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Application
 * Created by zhangping on 15/7/25.
 */
public class App extends Application {
    private static Context mContext;
    public static String DEVICE = "1";
    public static String DEVICEKEY = "";
    public static String AK = "AK";
    public static String SK = "SK";
    public static String SECURITYTOKEN = "securityToken";
    public static String EXPIRATION = "expiration";

    public static final String BUCKETNAME = "gongfu2";// 指明您的bucket是放在青岛数据中心

    public static final String ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";

    public static OSSService ossService = OSSServiceProvider.getService();

    public static LoginUserInfo loginUserInfo;

    public static HashMap<String, JSONObject> allstatusMap;

    public static HashMap<String, JSONObject> verifyMap;

    public static boolean isInHand = false;

    public static LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    public static ArrayList<Activity> openActivityList;

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyUtil.init(this);

        openActivityList = new ArrayList<>();
        allstatusMap = new HashMap<>();
        verifyMap = new HashMap<>();

        loginUserInfo = new LoginUserInfo();
        mContext = getApplicationContext();
        DEVICEKEY = AndroidUtil.getUdid(this);
        initOSS();
        initBaidu();

    }

    public static void addActivity(Activity activity) {
        openActivityList.add(activity);
    }

    public static void clearOpenActivity() {
        for (Activity activity : openActivityList) {
            try {
                if (activity != null && !activity.isFinishing())
                    activity.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initBaidu() {
        mLocationClient = new LocationClient(this);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系.国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocationClient.setLocOption(option);
    }

    private void initOSS() {
        // 打开调试log
        OSSLog.enableLog();

        // 初始化设置
        ossService.setApplicationContext(mContext);
        ossService.setGlobalDefaultHostId(ENDPOINT); // 设置region host 即 endpoint
        ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(60 * 1000); // 设置全局网络连接超时时间，默认30s
        conf.setSocketTimeout(60 * 1000); // 设置全局socket超时时间，默认30s
        conf.setMaxConnections(50); // 设置全局最大并发网络链接数, 默认50
        ossService.setClientConfiguration(conf);


    }

    public static Context getContext() {
        return mContext;
    }

    public static int checkStatus(String type) {
        JSONObject statusJSON = allstatusMap.get(type);
        return statusJSON == null ? 0 : statusJSON.optInt("status");
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            PreferenceUtils.setPrefString(App.getContext(), "location_time", location.getTime());
            PreferenceUtils.setPrefString(App.getContext(), "latitude", location.getLatitude() + "");
            PreferenceUtils.setPrefString(App.getContext(), "longitude", location.getLongitude() + "");
            PreferenceUtils.setPrefString(App.getContext(), "province", location.getProvince());

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }

            LogUtil.i("BaiduLocationApiDem", sb.toString());
        }


    }
}
