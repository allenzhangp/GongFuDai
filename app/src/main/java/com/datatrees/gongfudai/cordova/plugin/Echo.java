package com.datatrees.gongfudai.cordova.plugin;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * CordovaPlugin
 * Created by zhangping on 15/8/17.
 */
public class Echo extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            Log.d("cordova plugin", "android sent OK");
            callbackContext.success(message);
        } else {
            Log.d("cordova plugin", "android sent Fail");
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    @Override
    public boolean onOverrideUrlLoading(String url) {
        Log.d("test","onOverrideUrlLoading:"+url);
        return super.onOverrideUrlLoading(url);
    }
}