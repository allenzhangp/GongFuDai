package com.datatrees.gongfudai.information;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.base.BaseFragmentActivity;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
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

    @Bind(R.id.iv_idcard)
    ImageView iv_idcard;
    @Bind(R.id.iv_lxr)
    ImageView iv_lxr;
    @Bind(R.id.iv_yj)
    ImageView iv_yj;
    @Bind(R.id.iv_yys)
    ImageView iv_yys;
    @Bind(R.id.iv_ds)
    ImageView iv_ds;


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


    VerifyReciver verifyReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_supplementary);
        BK.bind(this);
        tvTitle.setText(R.string.info_sup);
        rlytIdcard.performClick();

        verifyReciver = new VerifyReciver();
        IntentFilter intentFilter = new IntentFilter(VerifyReciver.VERFY_RECEIVED);
        registerReceiver(verifyReciver, intentFilter);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (verifyReciver == null)
            verifyReciver = new VerifyReciver();
        IntentFilter intentFilter = new IntentFilter(VerifyReciver.VERFY_RECEIVED);
        registerReceiver(verifyReciver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (verifyReciver != null)
            unregisterReceiver(verifyReciver);
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
                if (idCardFragment == null || !idCardFragment.isFinish) {
                    ToastUtils.showShort(R.string.info_idcard_not_scc);
                    return;
                }
                changeBgColor(1);
                if (emeContactFragmfent == null)
                    emeContactFragmfent = new EmeContactFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, emeContactFragmfent).commit();
                break;
            case R.id.rlyt_yj:
                if (idCardFragment == null || !idCardFragment.isFinish) {
                    ToastUtils.showShort(R.string.info_idcard_not_scc);
                    return;
                }
                changeBgColor(2);
                if (emailValidFragmfent == null)
                    emailValidFragmfent = new EmailValidFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, emailValidFragmfent).commit();
                break;
            case R.id.rlyt_yys:
                if (idCardFragment == null || !idCardFragment.isFinish) {
                    ToastUtils.showShort(R.string.info_idcard_not_scc);
                    return;
                }
                changeBgColor(3);
                if (operatorValidFragmfent == null)
                    operatorValidFragmfent = new OperatorValidFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, operatorValidFragmfent).commit();
                break;
            case R.id.rlyt_ds:
                if (idCardFragment == null || !idCardFragment.isFinish) {
                    ToastUtils.showShort(R.string.info_idcard_not_scc);
                    return;
                }
                changeBgColor(4);
                if (electricityValidFragmfent == null)
                    electricityValidFragmfent = new ElectricityValidFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, electricityValidFragmfent).commit();
                break;
            case R.id.rlyt_xxyq:
                if (idCardFragment == null || !idCardFragment.isFinish) {
                    ToastUtils.showShort(R.string.info_idcard_not_scc);
                    return;
                }
                changeBgColor(5);
                if (infoSecurityFragmfent == null)
                    infoSecurityFragmfent = new InfoSecurityFragmfent();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, infoSecurityFragmfent).commit();
                break;
        }

    }

    private void changeBgColor(int position) {
        changeBgColor(position, true);
    }

    private void statusFail(int postion) {
        if (postion == 0) {
            stopAnimation(0);
            ivIDcardBg.setVisibility(View.GONE);
            iv_idcard.setEnabled(false);
        } else if (postion == 1) {
            stopAnimation(0);
            ivLxrBg.setVisibility(View.GONE);
            iv_lxr.setEnabled(false);
        } else if (postion == 2) {
            stopAnimation(0);
            ivYjBg.setVisibility(View.GONE);
            iv_yj.setEnabled(false);
        } else if (postion == 3) {
            stopAnimation(0);
            ivYysBg.setVisibility(View.GONE);
            iv_yys.setEnabled(false);
        } else if (postion == 4) {
            stopAnimation(0);
            ivDsBg.setVisibility(View.GONE);
            iv_ds.setEnabled(false);
        } else if (postion == 5) {
        }
    }

    private void statusOk(int postion) {
        if (postion == 0) {
            stopAnimation(0);
            ivIDcardBg.setVisibility(View.GONE);
            iv_idcard.setEnabled(true);
        } else if (postion == 1) {
            stopAnimation(0);
            ivLxrBg.setVisibility(View.GONE);
            iv_lxr.setEnabled(true);
        } else if (postion == 2) {
            stopAnimation(0);
            ivYjBg.setVisibility(View.GONE);
            iv_yj.setEnabled(true);
        } else if (postion == 3) {
            stopAnimation(0);
            ivYysBg.setVisibility(View.GONE);
            iv_yys.setEnabled(true);
        } else if (postion == 4) {
            stopAnimation(0);
            ivDsBg.setVisibility(View.GONE);
            iv_ds.setEnabled(true);
        } else if (postion == 5) {
        }
    }

    private void startAnimation(int postion) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        if (postion == 0) {
            animation.setAnimationListener(new RepeatAnimationLister(ivIDcardBg));
            ivIDcardBg.setVisibility(View.VISIBLE);
            ivIDcardBg.startAnimation(animation);
        } else if (postion == 1) {
            animation.setAnimationListener(new RepeatAnimationLister(ivLxrBg));
            ivLxrBg.setVisibility(View.VISIBLE);
            ivLxrBg.startAnimation(animation);
        } else if (postion == 2) {
            animation.setAnimationListener(new RepeatAnimationLister(ivYjBg));
            ivYjBg.setVisibility(View.VISIBLE);
            ivYjBg.startAnimation(animation);
        } else if (postion == 3) {
            animation.setAnimationListener(new RepeatAnimationLister(ivYysBg));
            ivYysBg.setVisibility(View.VISIBLE);
            ivYysBg.startAnimation(animation);
        } else if (postion == 4) {
            animation.setAnimationListener(new RepeatAnimationLister(ivDsBg));
            ivDsBg.setVisibility(View.VISIBLE);
            ivDsBg.startAnimation(animation);
        } else if (postion == 5) {
            animation.setAnimationListener(new RepeatAnimationLister(ivDsBg));
            ivXxyqBg.setVisibility(View.VISIBLE);
            ivXxyqBg.startAnimation(animation);
        }
    }

    private void stopAnimation(int postion) {
        Animation animation = null;
        if (postion == 0) {
            animation = ivIDcardBg.getAnimation();
        } else if (postion == 1) {
            animation = ivLxrBg.getAnimation();
        } else if (postion == 2) {
            animation = ivYjBg.getAnimation();
        } else if (postion == 3) {
            animation = ivYysBg.getAnimation();
        } else if (postion == 4) {
            animation = ivDsBg.getAnimation();
        } else if (postion == 5) {
            animation = ivXxyqBg.getAnimation();
        }
        if (null != animation) {
            animation.cancel();
        }
    }


    //当状态为通过或者成功之后背景动画变化
    private void checkstatus() {
        int idCardlStatus = App.checkStatus(ConstantUtils.ALLSTATUS_IDCARD);
        int contactsStatus = App.checkStatus(ConstantUtils.ALLSTATUS_ICE);
        int operatorStatus = App.checkStatus(ConstantUtils.ALLSTATUS_OPERATOR);
        int ecommerceStatus = App.checkStatus(ConstantUtils.ALLSTATUS_ECOMMERCE);
        int emailStatus = App.checkStatus(ConstantUtils.ALLSTATUS_EMAIL);

        if (idCardlStatus == 1) {
            startAnimation(0);
        } else if (idCardlStatus == 2) {
            statusOk(0);
        } else if (idCardlStatus == 3) {
            statusFail(0);
        } else {
            stopAnimation(0);
        }

        if (contactsStatus == 1) {
            startAnimation(1);
        } else if (contactsStatus == 2) {
            statusOk(1);
        } else if (contactsStatus == 3) {
            statusFail(1);
        } else {
            stopAnimation(1);
        }

        if (emailStatus == 1) {
            startAnimation(2);
        } else if (emailStatus == 2) {
            statusOk(2);
        } else if (emailStatus == 3) {
            statusFail(2);
        } else {
            stopAnimation(2);
        }


        if (operatorStatus == 1) {
            startAnimation(3);
        } else if (operatorStatus == 2) {
            statusOk(3);
        } else if (operatorStatus == 3) {
            statusFail(3);
        } else {
            stopAnimation(3);
        }


        if (ecommerceStatus == 1) {
            startAnimation(4);
        } else if (ecommerceStatus == 2) {
            statusOk(4);
        } else if (ecommerceStatus == 3) {
            statusFail(4);
        } else {
            stopAnimation(4);
        }


    }


    private void changeBgColor(int position, boolean invisible) {
        if (position == 0) {
            ivIDcardBg.setVisibility(View.VISIBLE);
            tvIDcard.setTextColor(getResources().getColor(R.color.white));
            if (invisible) {

                ivDsBg.setVisibility(View.INVISIBLE);
                ivLxrBg.setVisibility(View.INVISIBLE);
                ivXxyqBg.setVisibility(View.INVISIBLE);
                ivYjBg.setVisibility(View.INVISIBLE);
                ivYysBg.setVisibility(View.INVISIBLE);

                tvLxr.setTextColor(getResources().getColor(R.color.info_text));
                tvYj.setTextColor(getResources().getColor(R.color.info_text));
                tvYys.setTextColor(getResources().getColor(R.color.info_text));
                txXxyq.setTextColor(getResources().getColor(R.color.info_text));
                tvDs.setTextColor(getResources().getColor(R.color.info_text));
            }
        } else if (position == 1) {
            ivLxrBg.setVisibility(View.VISIBLE);
            tvLxr.setTextColor(getResources().getColor(R.color.white));
            if (invisible) {
                ivIDcardBg.setVisibility(View.INVISIBLE);
                ivDsBg.setVisibility(View.INVISIBLE);
                ivXxyqBg.setVisibility(View.INVISIBLE);
                ivYjBg.setVisibility(View.INVISIBLE);
                ivYysBg.setVisibility(View.INVISIBLE);

                tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
                tvYj.setTextColor(getResources().getColor(R.color.info_text));
                tvYys.setTextColor(getResources().getColor(R.color.info_text));
                txXxyq.setTextColor(getResources().getColor(R.color.info_text));
                tvDs.setTextColor(getResources().getColor(R.color.info_text));
            }
        } else if (position == 2) {
            ivYjBg.setVisibility(View.VISIBLE);
            tvYj.setTextColor(getResources().getColor(R.color.white));
            if (invisible) {
                ivIDcardBg.setVisibility(View.INVISIBLE);
                ivDsBg.setVisibility(View.INVISIBLE);
                ivLxrBg.setVisibility(View.INVISIBLE);
                ivXxyqBg.setVisibility(View.INVISIBLE);
                ivYysBg.setVisibility(View.INVISIBLE);

                tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
                tvLxr.setTextColor(getResources().getColor(R.color.info_text));
                tvYys.setTextColor(getResources().getColor(R.color.info_text));
                txXxyq.setTextColor(getResources().getColor(R.color.info_text));
                tvDs.setTextColor(getResources().getColor(R.color.info_text));
            }
        } else if (position == 3) {
            ivYysBg.setVisibility(View.VISIBLE);
            tvYys.setTextColor(getResources().getColor(R.color.white));
            if (invisible) {
                ivIDcardBg.setVisibility(View.INVISIBLE);
                ivDsBg.setVisibility(View.INVISIBLE);
                ivLxrBg.setVisibility(View.INVISIBLE);
                ivXxyqBg.setVisibility(View.INVISIBLE);
                ivYjBg.setVisibility(View.INVISIBLE);

                tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
                tvLxr.setTextColor(getResources().getColor(R.color.info_text));
                tvYj.setTextColor(getResources().getColor(R.color.info_text));
                txXxyq.setTextColor(getResources().getColor(R.color.info_text));
                tvDs.setTextColor(getResources().getColor(R.color.info_text));
            }
        } else if (position == 4) {
            ivDsBg.setVisibility(View.VISIBLE);
            tvDs.setTextColor(getResources().getColor(R.color.white));
            if (invisible) {
                ivIDcardBg.setVisibility(View.INVISIBLE);
                ivLxrBg.setVisibility(View.INVISIBLE);
                ivXxyqBg.setVisibility(View.INVISIBLE);
                ivYjBg.setVisibility(View.INVISIBLE);
                ivYysBg.setVisibility(View.INVISIBLE);

                tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
                tvLxr.setTextColor(getResources().getColor(R.color.info_text));
                tvYj.setTextColor(getResources().getColor(R.color.info_text));
                tvYys.setTextColor(getResources().getColor(R.color.info_text));
                txXxyq.setTextColor(getResources().getColor(R.color.info_text));
            }
        } else if (position == 5) {
            ivXxyqBg.setVisibility(View.VISIBLE);
            txXxyq.setTextColor(getResources().getColor(R.color.white));
            if (invisible) {
                ivIDcardBg.setVisibility(View.INVISIBLE);
                ivDsBg.setVisibility(View.INVISIBLE);
                ivLxrBg.setVisibility(View.INVISIBLE);
                ivYjBg.setVisibility(View.INVISIBLE);
                ivYysBg.setVisibility(View.INVISIBLE);

                tvIDcard.setTextColor(getResources().getColor(R.color.info_text));
                tvLxr.setTextColor(getResources().getColor(R.color.info_text));
                tvYj.setTextColor(getResources().getColor(R.color.info_text));
                tvYys.setTextColor(getResources().getColor(R.color.info_text));
                tvDs.setTextColor(getResources().getColor(R.color.info_text));
            }
        }
        checkstatus();
    }

    @OnClick(R.id.ibtn_back)
    public void goBack() {
        this.finish();
    }

    String key;

    public void confirm(String msg, String key) {
        this.key = key;
        App.isInHand = true;
        DialogHelper.alert(this, msg, getString(R.string.dialog_konw), listener).show();
    }

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


    class RepeatAnimationLister implements Animation.AnimationListener {
        View v;

        RepeatAnimationLister(View view) {
            this.v = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            v.startAnimation(animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
