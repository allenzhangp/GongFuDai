package com.datatrees.gongfudai.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.net.VolleyUtil;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by zhangping on 15/7/25.
 */
public class BaseFragment extends Fragment implements RespListener.OnRespError, RespListener.OnRespSuccess {
    Dialog loading;

    @Override
    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.onPageStart(getActivity().getClass().getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        } catch (Exception e) {
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            MobclickAgent.onPageEnd(getActivity().getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        } catch (Exception e) {
        }
    }

    protected void showLoading() {
        try {
            if (loading == null) {
                loading = ProgressDialog.show(getActivity(), null,
                        getString(R.string.loading_dialog_message), false, true);
            } else if (!loading.isShowing()) {
                loading.show();
            }
        } catch (Exception e) {
        }

    }

    protected void dismiss() {
        try {
            if (loading != null && loading.isShowing()) {
                loading.dismiss();
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BK.unBind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtil.getRequestQueue().cancelAll(this);
    }

    protected void executeRequest(Request<?> request) {
        showLoading();
        VolleyUtil.addRequest(request);
    }

    @Override
    public void onError(String error, String extras) {
        dismiss();
        if (StringUtils.isNotTrimBlank(error)) {
            ToastUtils.showShort(error);
        }
    }

    @Override
    public void onSuccess(JSONObject response, String extras) {
        dismiss();
        if (response == null)
            return;
    }

    public RespListener getRespListener() {
        RespListener respListener = new RespListener();
        respListener.onRespError = this;
        respListener.onRespSuccess = this;
        return respListener;
    }
}
