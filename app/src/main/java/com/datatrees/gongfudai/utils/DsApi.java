package com.datatrees.gongfudai.utils;

/**
 * Created by zhangping on 15/8/11.
 */
public class DsApi {
    private static final String HOST = "http://192.168.0.242:8080/service";

//    private static final String HOST = "http://192.168.3.115:8080";

    public static final String LIST = HOST + "%1$s";

    public static final String GETFEDERATIONTOKEN = "/oss/authorise";

    public static final String GETICR = "/certification/icr";

    public static final String CHECKICR = "/certification/check_ic";

    public static final String UPLOADCOTACTS = "/contacts/upload";

    public static final String ADDICE = "/contacts/ice/add";

    public static final String GETCONFIG = "/preconditions/config";

    public static final String PRECHECK = "/certification/precheck";

    public static final String COLLECTPRE="/preconditions/collect";

    public static final String GETPRESTATUS = "/preconditions/status";

    public static final String STATUSUPDATE = "/status/update";

    public static final String SUBMITVERFYCODE= "/preconditions/verifyCode";

    public static final String GEOS = "http://192.168.0.241:1818/tos";

}
