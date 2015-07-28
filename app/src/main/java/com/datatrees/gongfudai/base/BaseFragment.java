package com.datatrees.gongfudai.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.utils.BK;

/**
 * Created by zhangping on 15/7/25.
 */
public class BaseFragment extends Fragment{
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        BK.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BK.unBind(this);
    }
}
