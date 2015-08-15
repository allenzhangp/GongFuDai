package com.datatrees.gongfudai.information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.model.EmailValidModel;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.ui.WebClientActivity;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 电商验证
 * Created by zhangping on 15/8/11.
 */
public class ElectricityValidFragmfent extends BaseFragment {

    boolean getConfigSuccess = false;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    private String key;
    private static final int WEBCLINET_CODE = 8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_electricity_valid, container, false);
    }

    @OnClick(R.id.ibtn_taobao)
    public void onClickTD(View v) {
        if (!getConfigSuccess && urlDatas != null)
            return;
        JSONObject ecommerceJSON = App.allstatusMap.get("ecommerce");
        if (ecommerceJSON != null && (ecommerceJSON.optInt("status") == 1 || ecommerceJSON.optInt("status") == 2))
            return;
        EmailValidModel model = null;
        if (v.getId() == R.id.ibtn_taobao) {
            model = urlDatas.get("taobo");
        }
        if (model != null) {
            key = model.key;
            String[] endUrls = {model.endUrl};
            if (model != null) {
                startActivityForResult(new Intent(getActivity(), WebClientActivity.class).putExtra("insert_css", model.css).putExtra("visit_title", model.title).putExtra("visit_url", model.startUrl).putExtra("end_urls", endUrls), WEBCLINET_CODE);
            }
        }
    }

    boolean isTaobaoValid = false;

    private boolean isSubmitEnable() {
        return isTaobaoValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        String[] endCookies = data.getStringArrayExtra("end_cookies");
        String end_url = data.getStringExtra("end_url");
        String end_header = data.getStringExtra("end_header");
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", App.loginUserInfo.getUserId() + "");
        params.put("key", key);
        params.put("header", end_header);
        params.put("cookie", Arrays.toString(endCookies));
        params.put("url", end_url);
        RespListener respListener = new RespListener(key);
        respListener.onRespError = this;
        respListener.onRespSuccess = new RespListener.OnRespSuccess() {
            @Override
            public void onSuccess(String response, String extras) {
                if ("taobao".equals(extras))
                    isTaobaoValid = true;
                ToastUtils.showShort(R.string.upload_succeed);
                btn_submit.setEnabled(isSubmitEnable());
            }
        };
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LIST, DsApi.COLLECTPRE), respListener, params);
        executeRequest(request);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        BK.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getConfigSuccess) {
            CustomStringRequest request = new CustomStringRequest(Request.Method.GET, String.format(DsApi.LIST, DsApi.GETCONFIG), getRespListener());
            executeRequest(request);
        }
    }

    HashMap<String, EmailValidModel> urlDatas;

    @Override
    public void onSuccess(String response, String extras) {
        super.onSuccess(response, extras);
        JSONObject jsonResp = null;
        try {
            jsonResp = new JSONObject(response);
            getConfigSuccess = true;
            urlDatas = new HashMap<>();
            JSONArray jsonArray = jsonResp.optJSONArray("ecommerce");
            int lengh = jsonArray.length();
            for (int i = 0; i < lengh; i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                EmailValidModel model = new EmailValidModel();
                String key = obj.optString("key");
                model.key = key;
                model.endUrl = obj.optString("endUrl");
                model.css = obj.optString("css");
                model.image = obj.optString("image");
                model.startUrl = obj.optString("startUrl");
                model.title = obj.optString("title");
                urlDatas.put(key, model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
