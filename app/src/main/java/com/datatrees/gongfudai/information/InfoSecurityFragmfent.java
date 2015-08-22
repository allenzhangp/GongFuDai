package com.datatrees.gongfudai.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 申请额度
 * Created by zhangping on 15/8/11.
 */
public class InfoSecurityFragmfent extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_security, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BK.bind(this, view);
    }

    @OnClick(R.id.btn_qrsq)
    public void btnQrsq() {
        int statusID = App.checkStatus(ConstantUtils.ALLSTATUS_IDCARD);
        int statusICE = App.checkStatus(ConstantUtils.ALLSTATUS_ICE);
        int statusEmail = App.checkStatus(ConstantUtils.ALLSTATUS_EMAIL);
        int statusOper = App.checkStatus(ConstantUtils.ALLSTATUS_OPERATOR);
        int statusEco = App.checkStatus(ConstantUtils.ALLSTATUS_ECOMMERCE);

        InfoSupplementaryActivity supplementaryActivity = null;
        if (getActivity() instanceof InfoSupplementaryActivity) {
            supplementaryActivity = (InfoSupplementaryActivity) getActivity();
        }

        if (statusID != 2) {
            ToastUtils.showShort(R.string.info_idcard_not_scc);
            if (supplementaryActivity != null) supplementaryActivity.rlytIdcard.performClick();
        } else if (statusICE != 2) {
            ToastUtils.showShort(R.string.info_ice_not_scc);
            if (supplementaryActivity != null) supplementaryActivity.rlytLxr.performClick();
        } else if (statusEmail != 2) {
            ToastUtils.showShort(R.string.info_email_not_scc);
            if (supplementaryActivity != null) supplementaryActivity.rlytYj.performClick();
        } else if (statusOper != 2) {
            ToastUtils.showShort(R.string.info_opr_not_scc);
            if (supplementaryActivity != null) supplementaryActivity.rlytYys.performClick();
        } else if (statusEco != 2) {
            ToastUtils.showShort(R.string.info_ele_not_scc);
            if (supplementaryActivity != null) supplementaryActivity.rlytDs.performClick();
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", App.loginUserInfo.getUserId() + "");
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LIST, DsApi.CREDITAPPLY), getRespListener(), params);
        executeRequest(request);

    }

    @Override
    public void onSuccess(String response, String extras) {
        super.onSuccess(response, extras);
        getActivity().finish();
    }
}
