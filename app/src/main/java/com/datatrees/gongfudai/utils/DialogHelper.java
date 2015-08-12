package com.datatrees.gongfudai.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;

import com.datatrees.gongfudai.R;

/**
 * Created by zhangping on 15/8/12.
 */
public class DialogHelper {
    private DialogHelper() {
        // not need
    }

    public static ProgressDialog progressDialog(Context context, DialogInterface.OnClickListener listener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getResources().getString(R.string.uploading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条对话框//样式（水平，旋转）
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.dialog_cancle), listener);
        return progressDialog;
    }

    public static Dialog confrim(Context context, String msg,
                                 DialogInterface.OnClickListener positlistener, DialogInterface.OnClickListener negatlistener) {
        AlertDialog.Builder b = new Builder(context);
        b.setTitle(R.string.dialog_title);
        b.setMessage(msg);
        b.setPositiveButton(R.string.dialog_ok, positlistener);
        b.setNegativeButton(R.string.dialog_cancle, negatlistener);
        final Dialog d = b.create();
        return d;
    }

    public static AlertDialog alert(Context context, String msg,
                                    DialogInterface.OnClickListener listener) {
        AlertDialog.Builder b = new Builder(context);
        b.setTitle(R.string.dialog_title);
        b.setMessage(msg);
        b.setPositiveButton(R.string.dialog_ok, listener);
        final AlertDialog d = b.create();
        return d;
    }

}
