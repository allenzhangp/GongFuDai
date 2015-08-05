package com.datatrees.gongfudai.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.utils.BK;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangping on 15/7/25.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.onPageStart(getActivity().getClass().getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            MobclickAgent.onPageEnd(getActivity().getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        BK.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BK.unBind(this);
    }
}
