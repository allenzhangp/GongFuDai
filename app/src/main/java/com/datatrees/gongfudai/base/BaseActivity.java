package com.datatrees.gongfudai.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.net.VolleyUtil;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;
import com.datatrees.gongfudai.volley.Response;
import com.datatrees.gongfudai.volley.VolleyError;

/**
 * Created by zhangping on 15/7/25.
 */
public class BaseActivity extends Activity {
    Dialog loading;

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
