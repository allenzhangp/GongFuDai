package com.datatrees.gongfudai.information;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.model.ContactData;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ContactsAccessPublic;
import com.datatrees.gongfudai.utils.DialogHelper;

import java.util.List;

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
            }
        });
        rg_gx2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_ts2:
                        relation = 1;
                        break;
                    case R.id.rbtn_py2:
                        relation = 2;
                        break;
                    case R.id.rbtn_qs2:
                        relation = 0;
                        break;
                }
            }
        });
    }

    @OnClick({R.id.btn_contact_add, R.id.btn_contact_add2})
    public void chooseContact(View v) {
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
}
