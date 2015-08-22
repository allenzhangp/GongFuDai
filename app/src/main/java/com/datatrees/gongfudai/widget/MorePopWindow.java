package com.datatrees.gongfudai.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.datatrees.gongfudai.R;

/**
 * MorePopWindow
 * Created by zhangping on 15/8/21.
 */
public class MorePopWindow extends PopupWindow {
    private View conentView;


    private LinearLayout llayt_bdyhk;
    private LinearLayout llayt_sqte;

    public MorePopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.dialog_more_popup, null);
        llayt_bdyhk = (LinearLayout) conentView.findViewById(R.id.llayt_bdyhk);
        llayt_sqte = (LinearLayout) conentView.findViewById(R.id.llayt_sqte);

//        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    public void setBdyhkOnClickListener(View.OnClickListener onClickListener) {
        llayt_bdyhk.setOnClickListener(onClickListener);
    }

    public void setSqteOnClickListener(View.OnClickListener onClickListener) {
        llayt_sqte.setOnClickListener(onClickListener);
    }

}
