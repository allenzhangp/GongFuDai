package com.datatrees.gongfudai.cordova;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.cordova.plugin.Echo;
import com.datatrees.gongfudai.utils.BK;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * CordovaFragment
 * Created by zhangping on 15/8/17.
 */
public class CordovaFragment extends BaseFragment implements CordovaInterface {

    private static final int AUTHORIZATION_CODE = 1993;
    private static final int ACCOUNT_CODE = 1601;

    private AccountManager accountManager;

    @Bind(R.id.tutorialView)
    CordovaWebView cwv;

    String loadUrl;
    Echo activityResultCallback;

    public static CordovaFragment newInstance(String loadUrl) {
        Bundle args = new Bundle();
        args.putString("loadUrl", loadUrl);
        CordovaFragment fragment = new CordovaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cordova, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        BK.bind(this, view);
        Config.init(getActivity());
        loadUrl = getArguments().getString("loadUrl");
        cwv.loadUrl(Config.getStartUrl());
    }

    @Override
    public void startActivityForResult(CordovaPlugin cordovaPlugin, Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {
    }

    @Override
    public Object onMessage(String s, Object o) {
        return null;
    }

    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

}
