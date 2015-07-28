package com.datatrees.gongfudai.utils;

import android.app.Activity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by zhangping on 15/7/25.
 */
public class BK {
    public static void bind(Activity target) {
        ButterKnife.bind(target);
    }

    public static void bind(Object target, Activity source) {
        ButterKnife.bind(target, source);
    }

    public static void bind(Object target, View source) {
        ButterKnife.bind(target, source);
    }

    public static  void unBind(Object object){
        ButterKnife.unbind(object);
    }
}
