package com.datatrees.gongfudai.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.HomeActivity;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.model.LoginUserInfo;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.PreferenceUtils;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.com.datatrees.gesture_lock.GestureEditActivity;

/**
 * 验证码输入
 * Created by zhangping on 15/8/14.
 */
public class RegisterVerifyActivity extends BaseActivity {
    String phone;
    String password;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.et_verify)
    EditText et_verify;
    @Bind(R.id.ibtn_send_verify)
    Button ibtn_send_verify;
    @Bind(R.id.btn_next)
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_register_verify);
        BK.bind(this);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        password = intent.getStringExtra("password");
        et_verify.addTextChangedListener(textWatcher);
        tv_phone.setText(formatePhone(phone));
    }

    private String formatePhone(String phone) {
        if (StringUtils.isNotTrimBlank(phone) && phone.length() >= 11) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(phone.substring(0, 3));
            stringBuilder.append(" ");
            stringBuilder.append(phone.substring(3, 7));
            stringBuilder.append(" ");
            stringBuilder.append(phone.substring(7));
            return stringBuilder.toString();
        } else {
            return phone;
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btn_next.setEnabled(isRegisterEnable());
        }
    };

    private boolean isRegisterEnable() {
        return !TextUtils.isEmpty(et_verify.getText());
    }

    @OnClick(R.id.ibtn_send_verify)
    public void sendVerify() {
        CustomStringRequest request = new CustomStringRequest(Request.Method.GET, String.format(DsApi.LISTUSERCENTER, DsApi.GETSMS) + "/" + phone, getRespListener());
        executeRequest(request);

        ibtn_send_verify.setEnabled(false);
        ibtn_send_verify.setText(getString(R.string.login_count_down, 60));
        handler.sendEmptyMessageDelayed(60, 1000);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int count = msg.what;
            if (count <= 0) {
                ibtn_send_verify.setEnabled(true);
                ibtn_send_verify.setText(R.string.login_send_verify);
            } else {
                count--;
                ibtn_send_verify.setEnabled(false);
                ibtn_send_verify.setText(getString(R.string.login_count_down, count));
                handler.sendEmptyMessageDelayed(count, 1000);
            }
        }
    };

    private void requestLogin() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userpwd", password);
        params.put("mobile", phone);
        RespListener respListener = new RespListener();
        respListener.onRespError = this;
        respListener.onRespSuccess = new RespListener.OnRespSuccess() {
            @Override
            public void onSuccess(String response, String extras) {
                JSONObject jsonResp = null;
                try {
                    LoginUserInfo userInfo = new LoginUserInfo();
                    userInfo.setUserName(phone);
                    jsonResp = new JSONObject(response);

                    userInfo.setUserId(jsonResp.optLong("userid"));
                    userInfo.setToken(jsonResp.optString("token"));


                    PreferenceUtils.setPrefString(RegisterVerifyActivity.this, ConstantUtils.LOGIN_NAME, userInfo.getUserName());
//                    PreferenceUtils.setPrefString(RegisterVerifyActivity.this, ConstantUtils.LOGIN_PASSWORD, password);
                    PreferenceUtils.setPrefString(RegisterVerifyActivity.this, ConstantUtils.LOGIN_TOKEN, userInfo.getToken());
                    PreferenceUtils.setPrefLong(RegisterVerifyActivity.this, ConstantUtils.LOGIN_USERID, userInfo.getUserId());


                    App.loginUserInfo = userInfo;
                    App.clearOpenActivity();

                    startActivityForResult(new Intent(RegisterVerifyActivity.this, GestureEditActivity.class), ConstantUtils.SET_GESTURE_CODE);

                } catch (JSONException e) {
                }
            }
        };
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LISTUSERCENTER, DsApi.LOGIN), respListener, params);
        executeRequest(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == ConstantUtils.SET_GESTURE_CODE) {
            String inputCode = data.getStringExtra(GestureEditActivity.GESTURE_LOCKE_CODE);
            if (App.loginUserInfo != null && StringUtils.isNotTrimBlank(App.loginUserInfo.getToken())) {
                PreferenceUtils.setPrefString(RegisterVerifyActivity.this, ConstantUtils.GESTURE_CODE + App.loginUserInfo.getUserName(), inputCode);
                startActivity(new Intent(RegisterVerifyActivity.this, HomeActivity.class));
                RegisterVerifyActivity.this.finish();
            }
        }
    }


    boolean registerSucc = false;

    @OnClick(R.id.btn_next)
    public void register() {
        if (registerSucc) {
            requestLogin();
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", phone);
            params.put("userpwd", password);
            params.put("code", et_verify.getText().toString());

            RespListener respListener = new RespListener();
            respListener.onRespError = this;
            respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                @Override
                public void onSuccess(String response, String extras) {
                    registerSucc = true;
                    requestLogin();
                }
            };

            CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LISTUSERCENTER, DsApi.REGISTER), respListener, params);
            executeRequest(request);
        }
    }

}
