package com.datatrees.gongfudai;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.datatrees.gongfudai.base.BaseFragmentActivity;
import com.datatrees.gongfudai.cheats.CheatsFragment;
import com.datatrees.gongfudai.information.InfoSupplementaryActivity;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.service.GetStatusService;
import com.datatrees.gongfudai.service.VerifyReciver;
import com.datatrees.gongfudai.user.UserSettingActivity;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.DialogHelper;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.PreferenceUtils;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.utils.ViewUtils;
import com.datatrees.gongfudai.volley.Request;
import com.datatrees.gongfudai.widget.MorePopWindow;
import com.datatrees.gongfudai.widget.VerifyDialog;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 主页面
 * Created by zhangping on 15/8/10.
 */
public class HomeActivity extends BaseFragmentActivity implements CordovaInterface, App.OnMLocation {
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Bind(R.id.rg_home)
    public RadioGroup radioGroup;

    @Bind(R.id.tutorialView)
    CordovaWebView cordovaWebViewv;

    @Bind(R.id.ibtn_news)
    ImageButton ibtn_news;
    @Bind(R.id.ibtn_operation)
    ImageButton ibtn_operation;
    @Bind(R.id.ibtn_setting)
    ImageButton ibtn_setting;

    VerifyReciver verifyReciver;

    CordovaPlugin activityResultCallback;

    boolean keepRunning = true;

    @Bind(R.id.fragment_container)
    FrameLayout fragment_container;

    MorePopWindow morePopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BK.bind(this);

        this.startService(new Intent(this, GetStatusService.class));

        App.mLocationClient.start();

        Config.init(this);
        Config.addWhiteListEntry(DsApi.HOME_RUL, true);
        Config.addWhiteListEntry(DsApi.QZ_RUL, true);
        cordovaWebViewv.loadUrl(DsApi.HOME_RUL);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_sw:
                        mTvTitle.setText(R.string.home_sw);
                        ViewUtils.setGone(fragment_container, true);
                        ViewUtils.setGone(cordovaWebViewv, false);
                        ViewUtils.setInvisible(ibtn_news, false);
                        ViewUtils.setInvisible(ibtn_operation, true);
                        ViewUtils.setInvisible(ibtn_setting, true);
                        cordovaWebViewv.loadUrl(DsApi.HOME_RUL);
                        break;
                    case R.id.rbtn_qz:
                        mTvTitle.setText(R.string.home_qz);
                        ViewUtils.setGone(fragment_container, true);
                        ViewUtils.setGone(cordovaWebViewv, false);
                        ViewUtils.setInvisible(ibtn_news, true);
                        ViewUtils.setInvisible(ibtn_operation, false);
                        ViewUtils.setInvisible(ibtn_setting, true);
                        cordovaWebViewv.loadUrl(DsApi.QZ_RUL);
                        break;
                    case R.id.rbtn_mj:
                        mTvTitle.setText(R.string.home_mj);
                        ViewUtils.setGone(fragment_container, false);
                        ViewUtils.setGone(cordovaWebViewv, true);
                        ViewUtils.setInvisible(ibtn_news, true);
                        ViewUtils.setInvisible(ibtn_operation, true);
                        ViewUtils.setInvisible(ibtn_setting, false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheatsFragment()).commit();
                        break;
                }

            }
        });

        App.onMLocation = this;
        if (App.mLocationScu) {
            request();
        } else {
            LogUtil.e("startLocation---------");
            startLocation();
        }

    }

    public void toQzTab() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                radioGroup.check(R.id.rbtn_qz);
            }
        });
    }

    private void startLocation() {
        showLoading(R.string.loacatioin_dialog_message);
        App.mLocationClient.requestLocation();
    }

    boolean isAllow = false;

    private void request() {
        String latitude = PreferenceUtils.getPrefString(this, "latitude", "");
        String longitude = PreferenceUtils.getPrefString(this, "longitude", "");
        String province = PreferenceUtils.getPrefString(this, "province", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("lng", longitude);
        params.put("lat", latitude);
        params.put("province", province);
        params.put("userId", App.loginUserInfo.getUserId() + "");

        RespListener respListener = new RespListener();
        respListener.onRespError = this;
        respListener.onRespSuccess = new RespListener.OnRespSuccess() {
            @Override
            public void onSuccess(String response, String extras) {
                dismiss();
                JSONObject jsonResp;
                try {
                    jsonResp = new JSONObject(response);
                    int allow = jsonResp.optInt("allow");
                    int certify = jsonResp.optInt("certify");
                    if (allow == 0) {
                        ToastUtils.showShort(R.string.info_not_allow);
                    } else {
                        isAllow = true;
                        if (certify == 0) {
                            startActivity(new Intent(HomeActivity.this, InfoSupplementaryActivity.class));
                            ToastUtils.showShort(R.string.info_verify);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LIST, DsApi.PRECHECK), respListener, params);
        executeRequest(request);
    }


    @Override
    public void onSuccess(String response, String extras) {
        super.onSuccess(response, extras);
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.mLocationClient.stop();
        App.onMLocation = null;

    }

    @OnClick(R.id.ibtn_news)
    public void toHis() {

    }

    @OnClick(R.id.ibtn_setting)
    public void toSetting() {
        startActivity(new Intent(this, UserSettingActivity.class));
    }

    @OnClick(R.id.ibtn_operation)
    public void toOperation(View v) {
        if (morePopWindow == null) {
            morePopWindow = new MorePopWindow(this);
        }
        morePopWindow.showPopupWindow(v);
        morePopWindow.setBdyhkOnClickListener(bdyhkOnClickListener);
        morePopWindow.setSqteOnClickListener(sqteOnClickListener);
    }

    OnClickListener bdyhkOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            morePopWindow.dismiss();
        }
    };
    OnClickListener sqteOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            toInfoSupp();
            morePopWindow.dismiss();
        }
    };


    public void toInfoSupp() {
        if (!App.mLocationScu) {
            LogUtil.e("startLocation---------3455");
            startLocation();
            return;
        }
        if (isAllow)
            startActivity(new Intent(this, InfoSupplementaryActivity.class));
        else
            request();
    }

    String key;

    public void confirm(String msg, String key) {
        this.key = key;
        App.isInHand = true;
        DialogHelper.alert(this, msg, getString(R.string.dialog_konw), listener).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (verifyReciver != null)
            verifyReciver = new VerifyReciver();
        ((App) App.getContext()).setCurrentActivity(this);
        IntentFilter intentFilter = new IntentFilter(VerifyReciver.VERFY_RECEIVED);
        registerReceiver(verifyReciver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (verifyReciver != null)
            unregisterReceiver(verifyReciver);
        ((App) App.getContext()).setCurrentActivity(null);
    }

    public void inputDialog(String message, String imageBase64, String key) {
        this.key = key;
        App.isInHand = true;
        DialogHelper.verifyDialog(this, message, imageBase64, onVerifyOkClick).show();
    }

    VerifyDialog.OnVerifyOkClick onVerifyOkClick = new VerifyDialog.OnVerifyOkClick() {
        @Override
        public void onVerifyOkClick(Dialog dialog, String editString) {
            dialog.dismiss();

            HashMap<String, String> params = new HashMap<>();
            params.put("userId", App.loginUserInfo.getUserId() + "");
            params.put("key", key);
            params.put("code", editString);
            RespListener respListener = new RespListener(key);
            respListener.onRespError = new RespListener.OnRespError() {
                @Override
                public void onError(String errorResp, String extras) {
                    dismiss();
                    App.isInHand = false;
                }
            };
            respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                @Override
                public void onSuccess(String response, String extras) {
                    dismiss();
                    App.isInHand = false;
                    App.verifyMap.remove(extras);
                }
            };
            CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LIST, DsApi.SUBMITVERFYCODE), respListener, params);
            executeRequest(request);
        }
    };

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();

            HashMap<String, String> params = new HashMap<>();
            params.put("userId", App.loginUserInfo.getUserId() + "");
            params.put("key", key);
            RespListener respListener = new RespListener(key);
            respListener.onRespError = new RespListener.OnRespError() {
                @Override
                public void onError(String errorResp, String extras) {
                    dismiss();
                    App.isInHand = false;
                }
            };
            respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                @Override
                public void onSuccess(String response, String extras) {
                    dismiss();
                    App.isInHand = false;
                    JSONObject json = App.allstatusMap.get(extras);
                    if (json != null)
                        try {
                            json.putOpt("status", 0);
                        } catch (JSONException e) {
                        }
                }
            };
            CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LIST, DsApi.STATUSUPDATE), respListener, params);
            executeRequest(request);

        }
    };

    private long currentTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentTime < ConstantUtils.EXIT_TIME) {
                App.clearOpenActivity();
                GetStatusService.isFinish = true;
                stopService(new Intent(this, GetStatusService.class));
                finish();
            } else {
                currentTime = System.currentTimeMillis();
                ToastUtils.showShort(R.string.exit_tip);
                return true;
            }
        }
        return false;
    }

    /**
     * Launch an activity for which you would like a result when it finished. When this activity exits,
     * your onActivityResult() method is called.
     *
     * @param command     The command object
     * @param intent      The intent to start
     * @param requestCode The request code that is passed to callback to identify the activity
     */

    @Override
    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
        this.activityResultCallback = command;

        // If multitasking turned on, then disable it for activities that return results
        if (command != null) {
            this.keepRunning = false;
        }

        // Start activity
        super.startActivityForResult(intent, requestCode);


    }


    @Override
    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode       The request code originally supplied to startActivityForResult(),
     *                          allowing you to identify who this result came from.
     * @param resultCode        The integer result code returned by the child activity through its setResult().
     * @param data              An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        CordovaPlugin callback = this.activityResultCallback;
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, intent);
        }
    }


    @Override
    public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Object onMessage(String message, Object obj) {
        if (message.equalsIgnoreCase("exit")) {
            super.finish();
        }
        return null;
    }

    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public void onMLocation(boolean isSucc, String province) {
        dismiss();
        if (!isSucc)
            ToastUtils.showShort(R.string.location_failed);
    }
}
