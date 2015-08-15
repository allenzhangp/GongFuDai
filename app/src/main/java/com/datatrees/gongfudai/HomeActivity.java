package com.datatrees.gongfudai;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 主页面
 * Created by zhangping on 15/8/10.
 */
public class HomeActivity extends BaseFragmentActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Bind(R.id.rg_home)
    RadioGroup radioGroup;

    VerifyReciver verifyReciver;

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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TestFragment()).commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_sw:
                        mTvTitle.setText(R.string.home_sw);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TestFragment()).commit();
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
                finish();
            } else {
                currentTime = System.currentTimeMillis();
                ToastUtils.showShort(R.string.exit_tip);
                return true;
            }
        }
        return false;
    }
}
