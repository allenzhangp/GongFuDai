package com.datatrees.gongfudai.cheats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;

/**
 * 秘籍主页面
 * Created by zhangping on 15/8/11.
 */
public class CheatsFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cheats, container, false);
    }
}