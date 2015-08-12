package com.datatrees.gongfudai.information;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.base.BaseFragmentActivity;
import com.datatrees.gongfudai.utils.BK;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 信息补全主界面
 * Created by zhangping on 15/8/10.
 */
public class InfoSupplementaryActivity extends BaseFragmentActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.iv_idcard_bg)
    ImageView ivIDcardBg;
    @Bind(R.id.tv_idcard_tip)
    TextView tvIDcard;

    @Bind(R.id.iv_lxr_bg)
    ImageView ivLxrBg;
    @Bind(R.id.tv_lxr)
    TextView tvLxr;

    @Bind(R.id.iv_yj_bg)
    ImageView ivYjBg;
    @Bind(R.id.tv_yj)
    TextView tvYj;

    @Bind(R.id.iv_yys_bg)
    ImageView ivYysBg;
    @Bind(R.id.tv_yys)
    TextView tvYys;

    @Bind(R.id.iv_ds_bg)
    ImageView ivDsBg;
    @Bind(R.id.tv_ds)
    TextView tvDs;

    @Bind(R.id.iv_xxyq_bg)
    ImageView ivXxyqBg;
    @Bind(R.id.tv_xxyq)
    TextView txXxyq;


    @Bind(R.id.rlyt_idcard)
    RelativeLayout rlytIdcard;
    @Bind(R.id.rlyt_ds)
    RelativeLayout rlytDs;
    @Bind(R.id.rlyt_lxr)
    RelativeLayout rlytLxr;
    @Bind(R.id.rlyt_xxyq)
    RelativeLayout rlytXxyq;
    @Bind(R.id.rlyt_yj)
    RelativeLayout rlytYj;
    @Bind(R.id.rlyt_yys)
    RelativeLayout rlytYys;

    IDCardFragment idCardFragment = null;
    BaseFragment emeContactFragmfent = null;
    BaseFragment emailValidFragmfent = null;
    BaseFragment operatorValidFragmfent = null;
    BaseFragment electricityValidFragmfent = null;
    BaseFragment infoSecurityFragmfent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_supplementary);
        BK.bind(this);
        tvTitle.setText(R.string.info_sup);
        rlytIdcard.performClick();
    }

    @OnClick({R.id.rlyt_ds, R.id.rlyt_yys, R.id.rlyt_yj, R.id.rlyt_xxyq, R.id.rlyt_lxr, R.id.rlyt_idcard})
    public void clickRlyt(View view) {
        switch (view.getId()) {
            case R.id.rlyt_idcard:
                changeBgColor(0);
                if (idCardFragment == null)
                    idCardFragment = new IDCardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, idCardFragment).commit();
                break;
            case R.id.rlyt_lxr:
                if (idCardFragment == null || !idCardFragment.isFinish)
                    return;
                changeBgColor(1);
                if (emeContactFragmfent == null)
                    emeContactFragmfent = new EmeContactFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, emeContactFragmfent).commit();
                break;
            case R.id.rlyt_yj:
                if (idCardFragment == null || !idCardFragment.isFinish)
                    return;
                changeBgColor(2);
                if (emailValidFragmfent == null)
                    emailValidFragmfent = new EmailValidFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, emailValidFragmfent).commit();
                break;
            case R.id.rlyt_yys:
                if (idCardFragment == null || !idCardFragment.isFinish)
                    return;
                changeBgColor(3);
                if (operatorValidFragmfent == null)
                    operatorValidFragmfent = new OperatorValidFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, operatorValidFragmfent).commit();
                break;
            case R.id.rlyt_ds:
                if (idCardFragment == null || !idCardFragment.isFinish)
                    return;
                changeBgColor(4);
                if (electricityValidFragmfent == null)
                    electricityValidFragmfent = new ElectricityValidFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, electricityValidFragmfent).commit();
                break;
            case R.id.rlyt_xxyq:
                if (idCardFragment == null || !idCardFragment.isFinish)
                    return;
                changeBgColor(5);
                if (infoSecurityFragmfent == null)
                    infoSecurityFragmfent = new InfoSecurityFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, infoSecurityFragmfent).commit();
                break;
        }

    }

    private void changeBgColor(int position) {
        if (position == 0) {
            ivIDcardBg.setVisibility(View.VISIBLE);
            ivDsBg.setVisibility(View.INVISIBLE);
            ivLxrBg.setVisibility(View.INVISIBLE);
            ivXxyqBg.setVisibility(View.INVISIBLE);
            ivYjBg.setVisibility(View.INVISIBLE);
            ivYysBg.setVisibility(View.INVISIBLE);

            tvIDcard.setTextColor(getResources().getColor(R.color.white));
            tvLxr.setTextColor(getResources().getColor(R.color.info_text));
            tvYj.setTextColor(getResources().getColor(R.color.info_text));
            tvYys.setTextColor(getResources().getColor(R.color.info_text));
            txXxyq.setTextColor(getResources().getColor(R.color.info_text));
            tvDs.setTextColor(getResources().getColor(R.color.info_text));
        } else if (position == 1) {
            ivIDcardBg.setVisibility(View.INVISIBLE);
            ivDsBg.setVisibility(View.INVISIBLE);
            ivLxrBg.setVisibility(View.VISIBLE);
            ivXxyqBg.setVisibility(View.INVISIBLE);
            ivYjBg.setVisibility(View.INVISIBLE);
            ivYysBg.setVisibility(View.INVISIBLE);

            tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
            tvLxr.setTextColor(getResources().getColor(R.color.white));
            tvYj.setTextColor(getResources().getColor(R.color.info_text));
            tvYys.setTextColor(getResources().getColor(R.color.info_text));
            txXxyq.setTextColor(getResources().getColor(R.color.info_text));
            tvDs.setTextColor(getResources().getColor(R.color.info_text));
        } else if (position == 2) {
            ivIDcardBg.setVisibility(View.INVISIBLE);
            ivDsBg.setVisibility(View.INVISIBLE);
            ivLxrBg.setVisibility(View.INVISIBLE);
            ivXxyqBg.setVisibility(View.INVISIBLE);
            ivYjBg.setVisibility(View.VISIBLE);
            ivYysBg.setVisibility(View.INVISIBLE);

            tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
            tvLxr.setTextColor(getResources().getColor(R.color.info_text));
            tvYj.setTextColor(getResources().getColor(R.color.white));
            tvYys.setTextColor(getResources().getColor(R.color.info_text));
            txXxyq.setTextColor(getResources().getColor(R.color.info_text));
            tvDs.setTextColor(getResources().getColor(R.color.info_text));
        } else if (position == 3) {
            ivIDcardBg.setVisibility(View.INVISIBLE);
            ivDsBg.setVisibility(View.INVISIBLE);
            ivLxrBg.setVisibility(View.INVISIBLE);
            ivXxyqBg.setVisibility(View.INVISIBLE);
            ivYjBg.setVisibility(View.INVISIBLE);
            ivYysBg.setVisibility(View.VISIBLE);

            tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
            tvLxr.setTextColor(getResources().getColor(R.color.info_text));
            tvYj.setTextColor(getResources().getColor(R.color.info_text));
            tvYys.setTextColor(getResources().getColor(R.color.white));
            txXxyq.setTextColor(getResources().getColor(R.color.info_text));
            tvDs.setTextColor(getResources().getColor(R.color.info_text));
        } else if (position == 4) {
            ivIDcardBg.setVisibility(View.INVISIBLE);
            ivDsBg.setVisibility(View.VISIBLE);
            ivLxrBg.setVisibility(View.INVISIBLE);
            ivXxyqBg.setVisibility(View.INVISIBLE);
            ivYjBg.setVisibility(View.INVISIBLE);
            ivYysBg.setVisibility(View.INVISIBLE);

            tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
            tvLxr.setTextColor(getResources().getColor(R.color.info_text));
            tvYj.setTextColor(getResources().getColor(R.color.info_text));
            tvYys.setTextColor(getResources().getColor(R.color.info_text));
            txXxyq.setTextColor(getResources().getColor(R.color.info_text));
            tvDs.setTextColor(getResources().getColor(R.color.white));
        } else if (position == 5) {
            ivIDcardBg.setVisibility(View.INVISIBLE);
            ivDsBg.setVisibility(View.INVISIBLE);
            ivLxrBg.setVisibility(View.INVISIBLE);
            ivXxyqBg.setVisibility(View.VISIBLE);
            ivYjBg.setVisibility(View.INVISIBLE);
            ivYysBg.setVisibility(View.INVISIBLE);

            tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
            tvLxr.setTextColor(getResources().getColor(R.color.info_text));
            tvYj.setTextColor(getResources().getColor(R.color.info_text));
            tvYys.setTextColor(getResources().getColor(R.color.info_text));
            txXxyq.setTextColor(getResources().getColor(R.color.white));
            tvDs.setTextColor(getResources().getColor(R.color.info_text));
        }
    }

    @OnClick(R.id.ibtn_back)
    public void goBack() {
        this.finish();
    }
}
