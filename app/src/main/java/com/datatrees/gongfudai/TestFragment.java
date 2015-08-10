package com.datatrees.gongfudai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.base.BaseFragment;

/**
 * Created by zhangping on 15/8/10.
 */
public class TestFragment extends BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test,container,false);
    }

}
