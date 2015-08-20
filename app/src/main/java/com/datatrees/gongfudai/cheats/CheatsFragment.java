package com.datatrees.gongfudai.cheats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.user.UserInfoActivity;
import com.datatrees.gongfudai.utils.BK;

import butterknife.OnClick;

/**
 * 秘籍主页面
 * Created by zhangping on 15/8/11.
 */
public class CheatsFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cheats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BK.bind(this, view);
    }

    @OnClick(R.id.llayt_user_info)
    public void toUserInfo() {
        startActivity(new Intent(getActivity(), UserInfoActivity.class));
    }

}
