package com.datatrees.gongfudai.information;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.model.ContactData;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ContactsAccessPublic;
import com.datatrees.gongfudai.utils.DialogHelper;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 紧急联系人
 * Created by zhangping on 15/8/11.
 */
public class EmeContactFragmfent extends BaseFragment {
    @Bind(R.id.btn_contact_add)
    ImageButton btnContactAdd;
    @Bind(R.id.btn_contact_add2)
    ImageButton btnContactAdd2;

    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.et_idcard)
    EditText et_idcard;
    @Bind(R.id.rg_gx)
    RadioGroup rg_gx;

    @Bind(R.id.tv_phone2)
    TextView tv_phone2;
    @Bind(R.id.et_idcard2)
    EditText et_idcard2;
    @Bind(R.id.rg_gx2)
    RadioGroup rg_gx2;

    @Bind(R.id.btn_submit)
    Button btn_submit;

    static final int CONTACT_ADD = 10;
    private static final int CONTACT_ADD2 = 11;

    private int relation = -1;
    private int relation2 = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eme_contact, container, false);
    }

    private boolean submitEnable() {
        return relation >= 0 && relation2 >= 0 && !TextUtils.isEmpty(tv_phone2.getText()) && !TextUtils.isEmpty(et_idcard2.getText())
                && !TextUtils.isEmpty(tv_phone.getText()) && !TextUtils.isEmpty(et_idcard.getText());
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
            btn_submit.setEnabled(submitEnable());
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        BK.bind(this, view);

        tv_phone2.addTextChangedListener(textWatcher);
        tv_phone.addTextChangedListener(textWatcher);
        et_idcard.addTextChangedListener(textWatcher);
        et_idcard2.addTextChangedListener(textWatcher);

        rg_gx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_ts:
                        relation = 1;
                        break;
                    case R.id.rbtn_py:
                        relation = 2;
                        break;
                    case R.id.rbtn_qs:
                        relation = 0;
                        break;
                }
                btn_submit.setEnabled(submitEnable());
            }
        });
        rg_gx2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_ts2:
                        relation2 = 1;
                        break;
                    case R.id.rbtn_py2:
                        relation2 = 2;
                        break;
                    case R.id.rbtn_qs2:
                        relation2 = 0;
                        break;
                }
                btn_submit.setEnabled(submitEnable());
            }
        });
    }

    @OnClick({R.id.btn_contact_add, R.id.btn_contact_add2})
    public void chooseContact(View v) {

        JSONObject contactsJSON = App.allstatusMap.get("ice");
        if(contactsJSON != null && (contactsJSON.optInt("status") == 1 || contactsJSON.optInt("status") == 2))
            return;

        if (v.getId() == R.id.btn_contact_add) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, CONTACT_ADD);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            this.startActivityForResult(intent, CONTACT_ADD2);
        }
    }

    List<ContactData> datas = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        Uri contactData = data.getData();
        datas = ContactsAccessPublic.getPersonPhone(getActivity(), contactData);
        if (datas.size() <= 0)
            return;
        if (requestCode == CONTACT_ADD) {
            if (datas.size() > 1) {
                String items[] = new String[datas.size()];
                for (int i = 0; i < datas.size(); i++) {
                    items[i] = datas.get(i).getNumber();
                }
                DialogHelper.singleChoiceItems(getActivity(), items, itemClicklistener).show();
            } else {
                tv_phone.setText(datas.get(0).getNumber());
                et_idcard.setText(datas.get(0).getContactName());
            }
        } else if (requestCode == CONTACT_ADD2) {
            if (datas.size() > 1) {
                String items[] = new String[datas.size()];
                for (int i = 0; i < datas.size(); i++) {
                    items[i] = datas.get(i).getNumber();
                }
                DialogHelper.singleChoiceItems(getActivity(), items, itemClicklistener2).show();
            } else {
                tv_phone2.setText(datas.get(0).getNumber());
                et_idcard2.setText(datas.get(0).getContactName());
            }
        }
    }

    DialogInterface.OnClickListener itemClicklistener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            tv_phone.setText(datas.get(which).getNumber());
            et_idcard.setText(datas.get(which).getContactName());
        }
    };
    DialogInterface.OnClickListener itemClicklistener2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            tv_phone2.setText(datas.get(which).getNumber());
            et_idcard2.setText(datas.get(which).getContactName());
        }
    };

    boolean contactUPload = false;
    JSONArray contactArray = null;

    @OnClick(R.id.btn_submit)
    public void onSubmit() {
        showLoading();
        if (contactUPload == true) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", App.loginUserInfo.getUserId() + "");
            JSONArray jsonArray = new JSONArray();
            try {
                JSONObject obj1 = new JSONObject();
                obj1.put("name", et_idcard.getText().toString());
                obj1.put("phone", tv_phone.getText().toString());
                obj1.put("relation", relation + "");
                jsonArray.put(obj1);
                JSONObject obj2 = new JSONObject();
                obj2.put("name", et_idcard2.getText().toString());
                obj2.put("phone", tv_phone2.getText().toString());
                obj2.put("relation", relation2 + "");
                jsonArray.put(obj2);
            } catch (JSONException e) {
            }
            params.put("ice", jsonArray.toString());
            CustomStringRequest request = new CustomStringRequest(Request.Method.POST,String.format(DsApi.LIST,  DsApi.ADDICE), getRespListener(), params);
            executeRequest(request);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        contactArray = new JSONArray();
                        List<ContactData> contactDatas = ContactsAccessPublic.getContactsAll(getActivity(), null);
                        if (contactDatas.size() > 0) {
                            for (ContactData itmeContact : contactDatas) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("fn", itmeContact.getFn());
                                jsonObject.put("mn", itmeContact.getMn());
                                jsonObject.put("ln", itmeContact.getLn());
                                jsonObject.put("ext", itmeContact.getExt());
                                jsonObject.put("insDt", itmeContact.getInsDt());
                                jsonObject.put("updDt", itmeContact.getUpdDt());
                                jsonObject.put("cns", itmeContact.getPhoneArray().toString());
                                contactArray.put(jsonObject);
                            }
                        }
                        handler.sendEmptyMessage(1);
                    } catch (JSONException e) {
                    }
                }
            });
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (contactArray != null && contactArray.length() > 0) {
                HashMap<String, String> params = new HashMap<>();
                params.put("userId", App.loginUserInfo.getUserId() + "");
                params.put("contacts", contactArray.toString());
                params.put("total", contactArray.length() + "");
                params.put("device", App.DEVICE);
                params.put("deviceKey", App.DEVICEKEY);

                RespListener respListener = new RespListener();
                respListener.onRespError = EmeContactFragmfent.this;
                respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                    @Override
                    public void onSuccess(String response, String extras) {
                        contactUPload = true;
                        btn_submit.performClick();
                    }
                };

                CustomStringRequest request = new CustomStringRequest(Request.Method.POST,String.format(DsApi.LIST,  DsApi.UPLOADCOTACTS), respListener, params);
                executeRequest(request);
            }
        }
    };

    @Override
    public void onSuccess(String response, String extras) {
        super.onSuccess(response,extras);
        ToastUtils.showShort(R.string.upload_succeed);
        //next step
        if (getActivity() instanceof InfoSupplementaryActivity) {
            InfoSupplementaryActivity supplementaryActivity = (InfoSupplementaryActivity) getActivity();
            supplementaryActivity.rlytYj.performClick();
        }
    }
}
