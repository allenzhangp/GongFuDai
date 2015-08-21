package com.datatrees.gongfudai.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.utils.ConstantUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 申请额度
 * Created by zhangping on 15/8/11.
 */
public class InfoSecurityFragmfent extends BaseFragment {

    @Bind(R.id.btn_jxbc)
    Button btn_jxbc;

    @Bind(R.id.btn_qrsq)
    Button btn_qrsq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_security, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_jxbc)
    private void btnJxbc() {
        int statusID = App.checkStatus(ConstantUtils.ALLSTATUS_IDCARD);
        int statusICE = App.checkStatus(ConstantUtils.ALLSTATUS_ICE);
        int statusEmail = App.checkStatus(ConstantUtils.ALLSTATUS_EMAIL);
        int statusOper = App.checkStatus(ConstantUtils.ALLSTATUS_OPERATOR);
        int statusEco = App.checkStatus(ConstantUtils.ALLSTATUS_ECOMMERCE);

        InfoSupplementaryActivity supplementaryActivity = null;
        //next step
        if (getActivity() instanceof InfoSupplementaryActivity) {
            supplementaryActivity = (InfoSupplementaryActivity) getActivity();
        }

        if (statusID != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytIdcard.performClick();
        } else if (statusICE != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytLxr.performClick();
        } else if (statusEmail != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytYj.performClick();
        } else if (statusOper != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytYys.performClick();
        } else if (statusEco != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytDs.performClick();
        }
    }

    @OnClick(R.id.btn_qrsq)
    private void btnQrsq() {
        int statusID = App.checkStatus(ConstantUtils.ALLSTATUS_IDCARD);
        int statusICE = App.checkStatus(ConstantUtils.ALLSTATUS_ICE);
        int statusEmail = App.checkStatus(ConstantUtils.ALLSTATUS_EMAIL);
        int statusOper = App.checkStatus(ConstantUtils.ALLSTATUS_OPERATOR);
        int statusEco = App.checkStatus(ConstantUtils.ALLSTATUS_ECOMMERCE);
        InfoSupplementaryActivity supplementaryActivity = null;
        //next step
        if (getActivity() instanceof InfoSupplementaryActivity) {
            supplementaryActivity = (InfoSupplementaryActivity) getActivity();
        }

        if (statusID != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytIdcard.performClick();
        } else if (statusICE != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytLxr.performClick();
        } else if (statusEmail != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytYj.performClick();
        } else if (statusOper != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytYys.performClick();
        } else if (statusEco != 0) {
            if (supplementaryActivity != null) supplementaryActivity.rlytDs.performClick();
        }
    }

}
