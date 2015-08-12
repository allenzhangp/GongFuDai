package com.datatrees.gongfudai.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.net.VolleyUtil;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;
import com.datatrees.gongfudai.volley.Response;
import com.datatrees.gongfudai.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ucmed on 2015/8/5.
 */
public class BaseFragmentActivity extends FragmentActivity {
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

    protected void showLoading() {
        try {
            if (loading == null) {
                loading = ProgressDialog.show(this, null,
                        getString(R.string.loading_dialog_message), false, true);
            } else {
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
    public void onDestroy() {
        super.onDestroy();
        VolleyUtil.getRequestQueue().cancelAll(this);
    }

    protected void executeRequest(Request<?> request) {
        showLoading();
        VolleyUtil.addRequest(request);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                ToastUtils.showShort(error.getMessage());
            }
        };
    }
}
