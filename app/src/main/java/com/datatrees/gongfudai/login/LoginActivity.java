package com.datatrees.gongfudai.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.datatrees.gongfudai.utils.ValidUtils;
import com.datatrees.gongfudai.utils.ViewUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.com.datatrees.gesture_lock.GestureEditActivity;
import cn.com.datatrees.gesture_lock.GestureVerifyActivity;

/**
 * 登陆
 * Created by zhangping on 15/8/14.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.cb_show_pass)
    CheckBox checkBox;

    @Bind(R.id.ibtn_clear)
    ImageButton ibtnClear;

    @Bind(R.id.et_password)
    EditText etpassword;

    @Bind(R.id.et_username)
    EditText etUserName;

    @Bind(R.id.btn_submit)
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BK.bind(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        etpassword.addTextChangedListener(textNotNullWatcher);
        etpassword.addTextChangedListener(textWatcher);
        etUserName.addTextChangedListener(textWatcher);

        String phone = PreferenceUtils.getPrefString(this, ConstantUtils.LOGIN_NAME, "");
        String password = PreferenceUtils.getPrefString(this, ConstantUtils.LOGIN_PASSWORD, "");

        if (StringUtils.isNotTrimBlank(phone)) {
            etUserName.setText(phone);
        }
        if (StringUtils.isNotTrimBlank(password)) {
            etpassword.setText(password);
        }
        checkGesture();
    }

    public void checkGesture() {
        String userName = PreferenceUtils.getPrefString(this, ConstantUtils.LOGIN_NAME, "");
        String gestureCode = PreferenceUtils.getPrefString(this, ConstantUtils.GESTURE_CODE + userName, "");
        if (StringUtils.isNotTrimBlank(gestureCode)) {
            startActivityForResult(new Intent(this,
                            GestureVerifyActivity.class)
                            .putExtra(GestureVerifyActivity.PARAM_PHONE_NUMBER, userName)
                            .putExtra(GestureVerifyActivity.PARAM_INTENT_CODE, gestureCode),
                    ConstantUtils.VERIFY_GESTURE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == ConstantUtils.SET_GESTURE_CODE) {
            String gesture_code = data.getStringExtra(GestureEditActivity.GESTURE_LOCKE_CODE);
            PreferenceUtils.setPrefString(this, ConstantUtils.GESTURE_CODE + App.loginUserInfo.getUserName(), gesture_code);

            startHomeActivity();
            this.finish();

        } else if (requestCode == ConstantUtils.VERIFY_GESTURE_CODE) {
            RespListener respListener = new RespListener();
            respListener.onRespError = this;
            respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                @Override
                public void onSuccess(String response, String extras) {
                    dismiss();
                    LoginUserInfo userInfo = new LoginUserInfo();
                    userInfo.setUserName(PreferenceUtils.getPrefString(App.getContext(), ConstantUtils.LOGIN_NAME, ""));
                    userInfo.setUserId(PreferenceUtils.getPrefLong(App.getContext(), ConstantUtils.LOGIN_USERID, 0));
                    userInfo.setToken(response);
                    App.loginUserInfo = userInfo;

                    PreferenceUtils.setPrefString(App.getContext(), ConstantUtils.LOGIN_TOKEN, response);

                    startHomeActivity();
                    LoginActivity.this.finish();
                }
            };
            String userName = PreferenceUtils.getPrefString(this, ConstantUtils.LOGIN_NAME, "");
            String token = PreferenceUtils.getPrefString(this, ConstantUtils.LOGIN_TOKEN, "");

            CustomStringRequest request = new CustomStringRequest(Request.Method.GET, String.format(DsApi.TOKENRESET, token, userName), respListener);
            executeRequest(request);
        }
    }


    TextWatcher textNotNullWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(etpassword.getText())) {
                ViewUtils.setInvisible(ibtnClear, true);
            } else {
                ViewUtils.setInvisible(ibtnClear, false);
            }
        }
    };

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btn_submit.setEnabled(isLoginEnable());
        }
    };


    private boolean isLoginEnable() {
        return !TextUtils.isEmpty(etpassword.getText()) && !TextUtils.isEmpty(etUserName.getText())
                && ValidUtils.isValidPhoneNumber(etUserName.getText().toString());
    }


    @OnClick(R.id.ibtn_back)
    public void onBack() {
        this.finish();
    }

    @OnClick(R.id.ibtn_clear)
    public void onClearPass() {
        etpassword.getText().clear();
    }

    @OnClick(R.id.btn_submit)
    public void onSubmit() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userpwd", etpassword.getText().toString());
        params.put("mobile", etUserName.getText().toString());
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LISTUSERCENTER, DsApi.LOGIN), getRespListener(), params);
        executeRequest(request);
    }

    @Override
    public void onSuccess(String response, String extras) {
        super.onSuccess(response, extras);
        try {
            LoginUserInfo userInfo = new LoginUserInfo();
            userInfo.setUserName(etUserName.getText().toString());

            JSONObject jsonResp = new JSONObject(response);

            userInfo.setUserId(jsonResp.optLong("userid"));
            userInfo.setToken(jsonResp.optString("token"));

            PreferenceUtils.setPrefString(this, ConstantUtils.LOGIN_NAME, userInfo.getUserName());
//            PreferenceUtils.setPrefString(this, ConstantUtils.LOGIN_PASSWORD, etpassword.getText().toString());
            PreferenceUtils.setPrefString(this, ConstantUtils.LOGIN_TOKEN, userInfo.getToken());
            PreferenceUtils.setPrefLong(this, ConstantUtils.LOGIN_USERID, userInfo.getUserId());


            App.loginUserInfo = userInfo;
            App.clearOpenActivity();

            String gestureCode = PreferenceUtils.getPrefString(this, ConstantUtils.GESTURE_CODE + userInfo.getUserName(), "");
            if (StringUtils.isTrimBlank(gestureCode)) {
                startActivityForResult(new Intent(this, GestureEditActivity.class), ConstantUtils.SET_GESTURE_CODE);
            } else {
                startHomeActivity();
                LoginActivity.this.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.tv_forget_pass)
    public void passRest() {

        String phone = PreferenceUtils.getPrefString(this, ConstantUtils.LOGIN_NAME, "");
        String password = PreferenceUtils.getPrefString(this, ConstantUtils.LOGIN_PASSWORD, "");

        if (StringUtils.isNotTrimBlank(phone) && StringUtils.isNotTrimBlank(password)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("userpwd", phone);
            params.put("mobile", password);
            params.put("code", "123456");
            RespListener respListener = new RespListener();
            respListener.onRespError = this;
            respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                @Override
                public void onSuccess(String response, String extras) {
                    dismiss();
                }
            };
            CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LISTUSERCENTER, DsApi.PWDRESET), respListener, params);
            executeRequest(request);
        }
    }
}
