package com.datatrees.gongfudai.cordova.plugin;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.HomeActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * CordovaPlugin
 * Created by zhangping on 15/8/17.
 */
public class MCordovaPlugin extends CordovaPlugin {

    public static String HOME_ACTION_PUSH = "push";

    public static String LOAN_VIEWNAME = "LoanViewName";//申请贷款
    public static String CREDIT_VIEWNAME = "CreditViewName";//提升额度

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(HOME_ACTION_PUSH)) {
            String params0 = args.getString(0);
            homeAction(params0);
            return true;
        }
        return false;
    }

    public void homeAction(String param) {
        App app = (App) App.getContext();
        if (app.currentActivity != null && app.currentActivity instanceof HomeActivity) {
            if (param.equals(LOAN_VIEWNAME)) {
                HomeActivity homeActivity = (HomeActivity) app.currentActivity;
                homeActivity.toQzTab();
            } else if (param.equals(CREDIT_VIEWNAME)) {
                HomeActivity homeActivity = (HomeActivity) app.currentActivity;
                homeActivity.toInfoSupp();
            }

        }
    }

}
