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
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseActivity;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ValidUtils;
import com.datatrees.gongfudai.utils.ViewUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * register
 * Created by zhangping on 15/8/14.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.cb_show_pass)
    CheckBox checkBox;

    @Bind(R.id.ibtn_clear)
    ImageButton ibtnClear;

    @Bind(R.id.et_password)
    EditText etpassword;

    @Bind(R.id.et_username)
    EditText etUserName;

    @Bind(R.id.btn_next)
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
                && ValidUtils.isValidPhoneNumber(etUserName.getText().toString())
                && etpassword.getText().toString().length() >= 6;
    }

    @OnClick(R.id.ibtn_clear)
    public void onClearPass() {
        etpassword.getText().clear();
    }

    @OnClick(R.id.btn_next)
    public void onSubmit() {
        App.addActivity(this);
        startActivity(new Intent(this, RegisterVerifyActivity.class).putExtra("phone", etUserName.getText().toString()).putExtra("password", etpassword.getText().toString()));
    }

}
