package com.datatrees.gongfudai.utils;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CookieFormat
 * Created by zhangping on 15/8/22.
 */
public class CookieFormat {

    /**
     * @param cookies
     * @return
     */
    public static String listToString(Map<String, String> cookies) {
        String append = "; ";
        StringBuilder result = new StringBuilder();
        if (cookies != null && cookies.size() > 0) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                result.append(entry).append(append);
            }
            return result.toString().substring(0, (result.length() - 2));
        }
        return result.toString();
    }

    public String parserCookie(String[] cookieVals) {
        return listToString(this.parserCookietToMap(cookieVals));
    }

    public static Map<String, String> parserCookietToMap(String[] cookieVals) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        if (cookieVals != null && cookieVals.length > 0) {
            for (String one : cookieVals) {
                cookieFormat(one, cookieMap);
            }
        }
        return cookieMap;
    }

    public String parserCookie(String cookieVals) {
        return listToString(parserCookieToMap(cookieVals));
    }

    public static Map<String, String> parserCookieToMap(String cookieVals) {
        Map<String, String> cookieMap = null;
        if (cookieVals != null) {
            cookieMap = parserCookietToMap(cookieVals.split(";"));
        } else {
            cookieMap = new HashMap<String, String>();
        }
        return cookieMap;
    }

    private static void cookieFormat(String one, Map<String, String> cookieMap) {
        try {
            List<HttpCookie> cookies = HttpCookie.parse(one);
            HttpCookie cookie = cookies.get(0);
            if (!cookie.hasExpired()) {
                LogUtil.i("set cookie:\t" + cookie.getName() + " value: " + cookie.getValue());
                cookieMap.put(cookie.getName().trim(), cookie.getValue());
            } else {
                LogUtil.i("cookie:\t" + cookie.getName() + " value: " + cookie.getValue() + " has expired.");
            }
        } catch (Exception e) {
            LogUtil.e(one + " parser cookie error!", e.getMessage());
        }
    }
}
