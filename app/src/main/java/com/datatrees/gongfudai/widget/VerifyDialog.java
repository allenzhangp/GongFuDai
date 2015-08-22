package com.datatrees.gongfudai.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.utils.ViewUtils;

import butterknife.Bind;

/**
 * VerifyDialog
 * Created by zhangping on 15/8/14.
 */
public class VerifyDialog extends Dialog {

    @Bind(R.id.iv_verify)
    ImageView iv_verify;
    @Bind(R.id.et_verify)
    EditText et_verify;

    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;

    OnVerifyOkClick onVerifyOkClick;

    public VerifyDialog(Context context, String message) {
        super(context, R.style.VerifyDialog);
        init(context);
        setContent(message, false, null);
    }


    public VerifyDialog(Context context, String message, String imageBase64) {
        super(context, R.style.VerifyDialog);
        init(context);
        setContent(message, true, imageBase64);
    }

    public void setContent(String message, boolean hasImage, String imageBase64) {
        et_verify.setHint(message);
        if (hasImage && StringUtils.isNotTrimBlank(imageBase64)) {
            Bitmap bimap = ViewUtils.base64ToBitmap(imageBase64);
            iv_verify.setImageBitmap(bimap);
        } else {
            iv_verify.setVisibility(View.GONE);
        }

    }

    private void init(Context context) {
        final View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_verify, null);
        BK.bind(this, view);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);
        setContentView(view);
        getWindow().setLayout(screenWidth, LayoutParams.WRAP_CONTENT);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVerifyOkClick != null)
                    onVerifyOkClick.onVerifyOkClick(VerifyDialog.this, et_verify.getText().toString());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyDialog.this.dismiss();
                App.isInHand = false;
            }
        });
    }


    public void setOnVerifyOkClick(OnVerifyOkClick onVerifyOkClick) {
        this.onVerifyOkClick = onVerifyOkClick;
    }

    public void setOnVerifyCancelClick(View.OnClickListener onCancleClick) {
        btnCancel.setOnClickListener(onCancleClick);
    }

    public interface OnVerifyOkClick {
        public void onVerifyOkClick(Dialog dialog, String editString);
    }
}

