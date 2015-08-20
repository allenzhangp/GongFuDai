package com.datatrees.gongfudai;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@SuppressLint("DefaultLocale")
public final class AndroidUtil {
    private AndroidUtil() {
        // not need
    }

    private static final String TAG = "AndroidUtil";

    private static List<String> INVALID_IMEIs = new ArrayList<String>();

    static {
        INVALID_IMEIs.add("358673013795895");
        INVALID_IMEIs.add("004999010640000");
        INVALID_IMEIs.add("00000000000000");
        INVALID_IMEIs.add("000000000000000");
    }

    public static boolean isValidImei(String imei) {
        if (TextUtils.isEmpty(imei))
            return false;
        if (imei.length() < 10)
            return false;
        if (INVALID_IMEIs.contains(imei))
            return false;
        return true;
    }

    private static final String INVALID_ANDROIDID = "9774d56d682e549c";

    /**
     * 取得设备的唯一标识
     * <p>
     * imei -> androidId -> mac address -> uuid saved in sdcard
     *
     * @param context
     * @return
     */
    public static String getUdid(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        if (AndroidUtil.isValidImei(imei)) {
            return imei;
        }

        String androidId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId)
                && !INVALID_ANDROIDID.equals(androidId.toLowerCase())) {
            return androidId;
        }

        String macAddress = AndroidUtil.getWifiMacAddress(context);
        if (!TextUtils.isEmpty(macAddress)) {
            String udid = AndroidUtil.toMD5(macAddress + Build.MODEL
                    + Build.MANUFACTURER + Build.ID + Build.DEVICE);
            return udid;
        }

        return null;
    }

    public static String getWifiMacAddress(final Context context) {
        try {
            WifiManager wifimanager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifimanager.getConnectionInfo().getMacAddress();
            if (TextUtils.isEmpty(mac))
                return null;
            return mac;
        } catch (Exception e) {
            Log.d(TAG, "Get wifi mac address error", e);
        }
        return null;
    }

    public static String toMD5(String source) {
        if (null == source || "".equals(source))
            return null;
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(source.getBytes());
            return toHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) appendHex(result, buf[i]);
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
