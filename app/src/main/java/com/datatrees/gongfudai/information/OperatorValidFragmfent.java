package com.datatrees.gongfudai.information;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.datatrees.gongfudai.model.EmailValidModelFather;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.ui.WebClientActivity;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.DialogHelper;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 运营商验证
 * Created by zhangping on 15/8/11.
 */
public class OperatorValidFragmfent extends BaseFragment {
    boolean getConfigSuccess = false;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    private String website;
    private static final int WEBCLINET_CODE = 8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_operator_valid, container, false);
    }

    EmailValidModelFather modelFather = null;

    @OnClick({R.id.ibtn_lt, R.id.ibtn_yd, R.id.ibtn_dx})
    public void onClickOperator(View v) {
        if (!getConfigSuccess && urlDatas != null)
            return;
        JSONObject operatorJSON = App.allstatusMap.get("operator");
        if (operatorJSON != null && (operatorJSON.optInt("status") == 1 || operatorJSON.optInt("status") == 2))
            return;
        modelFather = null;
        if (v.getId() == R.id.ibtn_lt) {
            modelFather = urlDatas.get(ConstantUtils.KEY_10010);
        } else if (v.getId() == R.id.ibtn_yd) {
            modelFather = urlDatas.get(ConstantUtils.KEY_10086);
        } else if (v.getId() == R.id.ibtn_dx) {
            modelFather = urlDatas.get(ConstantUtils.KEY_189);
        }

        if (modelFather != null) {
            if (modelFather.operatorlist.size() > 0) {
                String[] items = new String[modelFather.operatorlist.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = modelFather.operatorlist.get(i).title;
                }
                DialogHelper.singleChoiceItems(getActivity(), items, itemClicklistener).show();
            } else {
                website = modelFather.website;
                String[] endUrls = {modelFather.endUrl};
                if (modelFather != null) {
                    startActivityForResult(new Intent(getActivity(), WebClientActivity.class).putExtra("usePCUA", modelFather.usePCUA).putExtra("insert_css", modelFather.css).putExtra("visit_title", modelFather.title).putExtra("visit_url", modelFather.startUrl).putExtra("end_urls", endUrls), WEBCLINET_CODE);
                }
            }
        }
    }

    DialogInterface.OnClickListener itemClicklistener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (modelFather != null) {
                EmailValidModel model = modelFather.operatorlist.get(which);
                website = model.website;
                String[] endUrls = {model.endUrl};
                if (model != null) {
                    startActivityForResult(new Intent(getActivity(), WebClientActivity.class).putExtra("usePCUA", model.usePCUA).putExtra("insert_css", model.css).putExtra("visit_title", model.title).putExtra("visit_url", model.startUrl).putExtra("end_urls", endUrls), WEBCLINET_CODE);
                }
            }

        }
    };

    boolean isLTValid = false;
    boolean isYDValid = false;
    boolean isDXValid = false;


    private boolean isSubmitEnable() {
        return isLTValid && isYDValid && isDXValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        String[] endCookies = data.getStringArrayExtra("end_cookies");
        StringBuilder endCookiesBuilder = new StringBuilder();
        boolean first = true;
        for (String endCookie : endCookies) {
            if (!first) {
                endCookiesBuilder.append(";");
            }
            first = false;
            endCookiesBuilder.append(endCookie);
        }

        String end_url = data.getStringExtra("end_url");
        String end_header = data.getStringExtra("end_header");
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", App.loginUserInfo.getUserId() + "");
        params.put("key", website);
        params.put("header", end_header);
        params.put("cookie", endCookiesBuilder.toString());
        params.put("url", end_url);
        RespListener respListener = new RespListener(website);
        respListener.onRespError = this;
        respListener.onRespSuccess = new RespListener.OnRespSuccess() {
            @Override
            public void onSuccess(String response, String extras) {
                dismiss();
                if (extras.contains(ConstantUtils.KEY_10010_NUMBER))
                    isLTValid = true;
                else if (extras.contains(ConstantUtils.KEY_10086_NUMBER))
                    isLTValid = true;
                else if (extras.contains(ConstantUtils.KEY_189_NUMBER))
                    isDXValid = true;

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
            CustomStringRequest request = new CustomStringRequest(Request.Method.GET, DsApi.getTokenUserId(String.format(DsApi.LIST, DsApi.GETCONFIG)), getRespListener());
            executeRequest(request);
        }
    }

    HashMap<String, EmailValidModelFather> urlDatas;

    @Override
    public void onSuccess(String response, String extras) {
        super.onSuccess(response, extras);
        JSONObject jsonResp = null;
        try {
            jsonResp = new JSONObject(response);
            getConfigSuccess = true;
            urlDatas = new HashMap<>();

            JSONArray operatorArray = jsonResp.optJSONArray("operator");
            JSONArray operatorListArray = jsonResp.optJSONArray("operatorList");

            for (int i = 0; i < operatorArray.length(); i++) {
                JSONObject operatorObj = operatorArray.optJSONObject(i);
                EmailValidModelFather model = new EmailValidModelFather();
                model.key = operatorObj.optString("key");
                model.endUrl = operatorObj.optString("endUrl");
                model.css = operatorObj.optString("css");
                model.image = operatorObj.optString("image");
                model.startUrl = operatorObj.optString("startUrl");
                model.title = operatorObj.optString("title");
                model.website = operatorObj.optString("website");
                model.usePCUA = operatorObj.optBoolean("usePCUA");

                int length = operatorListArray.length();
                for (int j = 0; j < length; j++) {
                    JSONObject objChild = operatorListArray.optJSONObject(j);
                    String key = objChild.optString("key");
                    if (key.contains(model.key)) {
                        EmailValidModel modelChilde = new EmailValidModel();
                        modelChilde.key = objChild.optString("key");
                        modelChilde.endUrl = objChild.optString("endUrl");
                        modelChilde.css = objChild.optString("css");
                        modelChilde.image = objChild.optString("image");
                        modelChilde.startUrl = objChild.optString("startUrl");
                        modelChilde.title = objChild.optString("title");
                        modelChilde.website = objChild.optString("website");
                        modelChilde.usePCUA = objChild.optBoolean("usePCUA");
                        model.operatorlist.add(modelChilde);
                    }
                }
                urlDatas.put(model.key, model);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_submit)
    public void toNestStep() {
        if (getActivity() instanceof InfoSupplementaryActivity) {
            InfoSupplementaryActivity supplementaryActivity = (InfoSupplementaryActivity) getActivity();
            supplementaryActivity.rlytDs.performClick();
        }
    }

}
