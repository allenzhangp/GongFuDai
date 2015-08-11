package com.datatrees.gongfudai.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;

/**
 *  邮箱验证
 * Created by zhangping on 15/8/11.
 */
public class EmailValidFragmfent extends BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_valid, container, false);
    }

}
