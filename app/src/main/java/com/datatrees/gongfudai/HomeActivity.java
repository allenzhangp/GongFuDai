package com.datatrees.gongfudai;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.datatrees.gongfudai.base.BaseFragmentActivity;
import com.datatrees.gongfudai.cheats.CheatsFragment;
import com.datatrees.gongfudai.cordova.CordovaFragment;
import com.datatrees.gongfudai.information.InfoSupplementaryActivity;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.service.GetStatusService;
import com.datatrees.gongfudai.service.VerifyReciver;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.DialogHelper;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;
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
public class HomeActivity extends BaseFragmentActivity implements CordovaInterface {
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Bind(R.id.rg_home)
    RadioGroup radioGroup;

    @Bind(R.id.tutorialView)
    CordovaWebView cordovaWebViewv;

    VerifyReciver verifyReciver;

    CordovaPlugin activityResultCallback;

    boolean keepRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BK.bind(this);

        this.startService(new Intent(this, GetStatusService.class));

        verifyReciver = new VerifyReciver();
        IntentFilter intentFilter = new IntentFilter(VerifyReciver.VERFY_RECEIVED);
        registerReceiver(verifyReciver, intentFilter);

        App.mLocationClient.start();

        Config.init(this);
        Config.addWhiteListEntry(DsApi.HOME_RUL, true);
        cordovaWebViewv.loadUrl(DsApi.HOME_RUL);

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CordovaFragment.newInstance(DsApi.HOME_RUL)).commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_sw:
                        mTvTitle.setText(R.string.home_sw);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CordovaFragment.newInstance(DsApi.HOME_RUL)).commit();
                        break;
                    case R.id.rbtn_qz:
                        mTvTitle.setText(R.string.home_qz);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TestFragment()).commit();
                        break;
                    case R.id.rbtn_mj:
                        mTvTitle.setText(R.string.home_mj);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheatsFragment()).commit();
                        break;
                }

            }
        });

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
        unregisterReceiver(verifyReciver);
    }

    @OnClick(R.id.ibtn_news)
    public void toHis() {

    }

    @OnClick(R.id.ibtn_operation)
    public void toOperation() {
        startActivity(new Intent(this, InfoSupplementaryActivity.class));
    }


    String key;

    public void confirm(String msg, String key) {
        this.key = key;
        DialogHelper.alert(this, msg, getString(R.string.dialog_konw), listener).show();
    }

    public void inputDialog(String message, String imageBase64, String key) {
        this.key = key;
        DialogHelper.verifyDialog(this, message, imageBase64, onVerifyOkClick);
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
            respListener.onRespError = HomeActivity.this;
            respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                @Override
                public void onSuccess(String response, String extras) {
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
            respListener.onRespError = HomeActivity.this;
            respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                @Override
                public void onSuccess(String response, String extras) {
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

}
