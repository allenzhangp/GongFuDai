package com.datatrees.gongfudai.model;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.PreferenceUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * @author: zhouzhuo<yecan.xyc@alibaba-inc.com>
 * Apr 30, 2015
 */
public class FederationTokenGetter {

    private static FederationToken token;

    public static FederationToken getToken(long userId) {
        token = getTokenFromServer(userId);
        return token;
    }

    private static FederationToken getTokenFromServer(long userId) {
        String queryUrl = String.format(DsApi.LIST, DsApi.GETFEDERATIONTOKEN) + "?userId=" + userId;
        String responseStr = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(queryUrl);
            HttpResponse hr = client.execute(httpGet);
            responseStr = EntityUtils.toString(hr.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseStr == null) {
            return null;
        }
        try {
            LogUtil.i(responseStr);
            JSONObject jsonObject = new JSONObject(responseStr);

            int code = jsonObject.optInt("code");
            String errorMsg = jsonObject.optString("errorMsg");
            if(code != 200){
                return null;
            }
            JSONObject dataObj = jsonObject.optJSONObject("data");
            String ak = dataObj.getString("ak");
            String sk = dataObj.getString("sk");
            String securityToken = dataObj.getString("tempToken");
            long expireTime = dataObj.getLong("expiration");
            PreferenceUtils.setPrefString(App.getContext(),App.AK,ak);
            PreferenceUtils.setPrefString(App.getContext(),App.SK,sk);
            PreferenceUtils.setPrefString(App.getContext(),App.SECURITYTOKEN,securityToken);
            PreferenceUtils.setPrefLong(App.getContext(),App.EXPIRATION,expireTime);
            return new FederationToken(ak, sk, securityToken, expireTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
