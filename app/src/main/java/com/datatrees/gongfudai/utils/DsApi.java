package com.datatrees.gongfudai.utils;

import com.datatrees.gongfudai.App;

/**
 * DsApi
 * Created by zhangping on 15/8/11.
 */
public class DsApi {

    /**
     * h5页面
     */
    private static final String H5_HOST = "http://91gfd.com.cn/h5";
//    private static final String H5_HOST = "http://192.168.0.241:1818";


    /**
     * 用户中心
     */
    private static final String USERCENTERHOST = "http://91gfd.com.cn/usercenter/api";
//    private static final String USERCENTERHOST = " http://192.168.0.241:12172/usercenter/api";

    /**
     * 信息补全
     */
    private static final String HOST = "http://91gfd.com.cn/gongfudai";
//    private static final String HOST = "http://192.168.0.242:8080/service";
//    private static final String HOST = "http://192.168.3.110:14501";


    //cordova
    public static final String HOME_RUL = H5_HOST + "/home?platform=android";
    public static final String QZ_RUL = H5_HOST + "/application?platform=android";
    public static final String TOS_RUL = H5_HOST + "/tos?platform=android";


    /**
     * 用户中心接口模块
     */

    public static final String LISTUSERCENTER = USERCENTERHOST + "%1$s";

    public static final String GETSMS = "/sms/";

    public static final String REGISTER = "/customers/register";

    public static final String LOGIN = "/customers/login";

    public static final String TOKENRESET = USERCENTERHOST + "/token/%1$s/reset/%2$s";

    public static final String PWDRESET = "/customers/pwdreset";


    /**
     * 登陆成功之后接口模块
     */
    public static final String LIST = HOST + "%1$s";


    public static final String GETFEDERATIONTOKEN = "/oss/authorise";

    public static final String GETICR = "/certification/icr";

    public static final String CHECKICR = "/certification/check_ic";

    public static final String GETICR2 = "/certification/get_ic";

    public static final String UPLOADCOTACTS = "/contacts/upload";

    public static final String ADDICE = "/contacts/ice/add";

    public static final String GETICE = "/contacts/ice/get";

    public static final String GETCONFIG = "/preconditions/config";

    public static final String PRECHECK = "/certification/precheck";

    public static final String COLLECTPRE = "/preconditions/collect";

    public static final String GETPRESTATUS = "/preconditions/status";

    public static final String STATUSUPDATE = "/preconditions/postStatus";

    public static final String SUBMITVERFYCODE = "/preconditions/verifyCode";

    public static final String CREDITAPPLY = "/loan/credit/apply";


    public static String getTokenUserId(String url) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (App.loginUserInfo != null && StringUtils.isNotTrimBlank(url)) {
            if (url.contains("?")) {
                urlBuilder.append("&userId=").append(App.loginUserInfo.getUserId()).append("&token=").append(App.loginUserInfo.getToken());
            } else {
                urlBuilder.append("?userId=").append(App.loginUserInfo.getUserId()).append("&token=").append(App.loginUserInfo.getToken());
            }
        }
        return urlBuilder.toString();
    }
}
