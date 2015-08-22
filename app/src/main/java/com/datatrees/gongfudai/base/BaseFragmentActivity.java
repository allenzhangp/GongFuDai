package com.datatrees.gongfudai.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.net.VolleyUtil;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;
import com.umeng.analytics.MobclickAgent;

/**
 * basefragmentactivity
 * Created by ucmed on 2015/8/5.
 */
public class BaseFragmentActivity extends FragmentActivity implements RespListener.OnRespError, RespListener.OnRespSuccess {
    Dialog loading;

    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.openActivityDurationTrack(false);
            MobclickAgent.onResume(this);       //统计时长
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        super.onPause();
        try {
            MobclickAgent.onPause(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showLoading(int msg) {
        try {
            if (loading == null) {
                loading = ProgressDialog.show(this, null,
                        getString(msg), false, true);
            } else {
                if (!loading.isShowing())
                    loading.show();
            }
        } catch (Exception e) {
        }

    }

    protected void showLoading() {
        showLoading(R.string.loading_dialog_message);
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
    public void onDestroy() {
        super.onDestroy();
        VolleyUtil.getRequestQueue().cancelAll(this);
    }

    protected void executeRequest(Request<?> request, boolean showLoading) {
        if (showLoading) showLoading();
        VolleyUtil.addRequest(request);
    }

    protected void executeRequest(Request<?> request) {
        executeRequest(request, true);
    }

    @Override
    public void onError(String error, String extras) {
        dismiss();
        if (StringUtils.isNotTrimBlank(error)) {
            ToastUtils.showShort(error);
        }
    }

    @Override
    public void onSuccess(String response, String extras) {
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
