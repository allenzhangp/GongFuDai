package com.datatrees.gongfudai.information;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.model.OSSFederationToken;
import com.alibaba.sdk.android.oss.model.StsTokenGetter;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.alibaba.sdk.android.oss.storage.TaskHandler;
import com.datatrees.camera.CameraActivity;
import com.datatrees.camera.PhotoPreActivity;
import com.datatrees.gongfudai.App;
import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragment;
import com.datatrees.gongfudai.model.FederationToken;
import com.datatrees.gongfudai.model.FederationTokenGetter;
import com.datatrees.gongfudai.net.CustomStringRequest;
import com.datatrees.gongfudai.net.RespListener;
import com.datatrees.gongfudai.utils.BK;
import com.datatrees.gongfudai.utils.ConstantUtils;
import com.datatrees.gongfudai.utils.DialogHelper;
import com.datatrees.gongfudai.utils.DsApi;
import com.datatrees.gongfudai.utils.LogUtil;
import com.datatrees.gongfudai.utils.PreferenceUtils;
import com.datatrees.gongfudai.utils.StringUtils;
import com.datatrees.gongfudai.utils.ToastUtils;
import com.datatrees.gongfudai.utils.ViewUtils;
import com.datatrees.gongfudai.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 身份证拍照验证
 * Created by zhangping on 15/8/11.
 */
public class IDCardFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.iv_take_photo1)
    ImageView iv_take_photo1;
    @Bind(R.id.iv_take_photo2)
    ImageView iv_take_photo2;
    @Bind(R.id.iv_take_photo3)
    ImageView iv_take_photo3;

    @Bind(R.id.tv_take_photo1)
    TextView tv_take_photo1;
    @Bind(R.id.tv_take_photo2)
    TextView tv_take_photo2;
    @Bind(R.id.tv_take_photo3)
    TextView tv_take_photo3;

    @Bind(R.id.llyt_take_tip1)
    LinearLayout llyt_take_tip1;
    @Bind(R.id.llyt_take_tip2)
    LinearLayout llyt_take_tip2;
    @Bind(R.id.llyt_take_tip3)
    LinearLayout llyt_take_tip3;

    @Bind(R.id.et_idcard)
    EditText et_idcard;
    @Bind(R.id.et_real_name)
    EditText et_real_name;

    @Bind(R.id.tv_realname_update)
    TextView tv_realname_update;
    @Bind(R.id.tv_idcard_update)
    TextView tv_idcard_update;
    @Bind(R.id.llyt_user_info)
    LinearLayout llyt_user_info;

    @Bind(R.id.btn_submit)
    Button btn_submit;

    private OSSBucket bucket;

    private int step = 0;//第几步骤
    private ProgressDialog progressDialog;
    private TaskHandler tk;

    public boolean isFinish = false;
    private boolean isUploadPhotoFinish = false;//三张照片拍完上传成功之后为true

    private long timestamp = 0;

    String imagePath;

    boolean hasGetInfo = false;//已经上传成功的

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_idcard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        BK.bind(this, view);
        bucket = App.ossService.getOssBucket(App.BUCKETNAME);
        et_idcard.addTextChangedListener(textWatcher);
        et_idcard.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ViewUtils.hideInputPanel(getActivity());
                    et_idcard.setEnabled(false);
                    return true;
                }
                return false;
            }
        });

        et_real_name.addTextChangedListener(textWatcher);
        et_real_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ViewUtils.hideInputPanel(getActivity());
                    et_real_name.setEnabled(false);
                    return true;
                }
                return false;
            }
        });

        timestamp = System.currentTimeMillis();

        llyt_take_tip1.setOnClickListener(this);
        llyt_take_tip2.setOnClickListener(null);
        llyt_take_tip3.setOnClickListener(null);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btn_submit.setEnabled(submitEnable());
        }
    };

    private boolean submitEnable() {
        int status = App.checkStatus("idcard");
        return status != 1 && status != 2 && status != 3 && !TextUtils.isEmpty(et_idcard.getText()) && !TextUtils.isEmpty(et_real_name.getText());
    }

    JSONObject idJSON = null;

    @Override
    public void onResume() {
        super.onResume();
        int status = App.checkStatus("idcard");

        if (status == 1 || status == 2 || status == 3) {
            isFinish = true;
            isUploadPhotoFinish = true;
            if (hasGetInfo && idJSON != null) {
                String name = idJSON.optString("name");
                String idNumber = idJSON.optString("idNumber");
                llyt_user_info.setVisibility(View.VISIBLE);
                et_real_name.setText(name);
                et_idcard.setText(idNumber);
            } else {
                RespListener respListener = new RespListener();
                respListener.onRespError = this;
                respListener.onRespSuccess = new RespListener.OnRespSuccess() {
                    @Override
                    public void onSuccess(String response, String extras) {
                        dismiss();
                        if (response == null)
                            return;
                        hasGetInfo = true;
                        idJSON = null;
                        try {
                            idJSON = new JSONObject(response);
                            String name = idJSON.optString("name");
                            String idNumber = idJSON.optString("idNumber");
                            llyt_user_info.setVisibility(View.VISIBLE);
                            et_real_name.setText(name);
                            et_idcard.setText(idNumber);
                        } catch (JSONException e) {
                        }


                    }
                };

                CustomStringRequest request = new CustomStringRequest(Request.Method.GET, DsApi.getTokenUserId(String.format(DsApi.LIST, DsApi.GETICR2)), respListener);
                executeRequest(request);
            }
        }
    }

    DialogInterface.OnClickListener uploadListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (tk != null) {
                tk.cancel();
                tk = null;
            }
        }
    };

    @OnClick({R.id.tv_idcard_update, R.id.tv_realname_update})
    public void onUpdateClick(View view) {
        int status = App.checkStatus("idcard");
        if (status == 2 || status == 3 || status == 1)
            return;
        if (view.getId() == R.id.tv_realname_update) {
            et_real_name.setFocusable(true);
            et_real_name.setEnabled(true);
            ViewUtils.showInputPanel(getActivity());
        } else {
            et_idcard.setFocusable(true);
            et_idcard.setEnabled(true);
            ViewUtils.showInputPanel(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == ConstantUtils.TAKE_PHOTO_CODE) {
            imagePath = data.getStringExtra(PhotoPreActivity.IMAGE_PATH);
            if (StringUtils.isNotBlank(imagePath)) {
                uploadFile2OSS(imagePath);
                if (progressDialog == null)
                    progressDialog = DialogHelper.progressDialog(getActivity(), uploadListener);
                progressDialog.show();
            }
        }
    }

    private Bitmap getBimap(String filePath) {
        Bitmap realImage;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        options.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
        realImage = BitmapFactory.decodeFile(filePath, options);
        return realImage;
    }

    private void uploadFile2OSS(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        long expirationTime = timestamp;
        OSSFile ossFile = App.ossService.getOssFile(bucket, App.loginUserInfo.getUserId() + File.separator + expirationTime + File.separator + fileName + ".jpg");
        try {
            ossFile.setUploadFilePath(filePath, "file");

            TaskHandler tk = ossFile.uploadInBackground(new SaveCallback() {
                @Override
                public void onProgress(String objectKey, int byteCount, int totalSize) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.setMax(totalSize);
                        progressDialog.setProgress(byteCount);
                    }
                }

                @Override
                public void onFailure(String objectKey, OSSException ossException) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            ToastUtils.showShort(R.string.upload_fail);
                        }
                    });
                }

                @Override
                public void onSuccess(String objectKey) {
                    LogUtil.i(objectKey);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Bitmap bitmap = getBimap(imagePath);
                            Drawable drawable = new BitmapDrawable(null, bitmap);
                            if (step == 0) {
                                iv_take_photo1.setImageDrawable(null);
                                iv_take_photo1.setBackgroundDrawable(null);
                                iv_take_photo1.setBackgroundDrawable(drawable);

                                if (!isUploadPhotoFinish) {
                                    iv_take_photo2.setImageDrawable(null);
                                    iv_take_photo2.setBackgroundResource(R.drawable.bg_take_photo_sel);
                                    tv_take_photo2.setTextColor(getResources().getColor(R.color.black_txt));
                                    llyt_take_tip2.setOnClickListener(IDCardFragment.this);
                                }

                            } else if (step == 1) {
                                iv_take_photo2.setBackgroundDrawable(null);
                                iv_take_photo2.setImageDrawable(null);
                                iv_take_photo2.setBackgroundDrawable(drawable);
                                if (!isUploadPhotoFinish) {
                                    iv_take_photo3.setImageDrawable(null);
                                    iv_take_photo3.setBackgroundResource(R.drawable.bg_take_photo_sel);
                                    tv_take_photo3.setTextColor(getResources().getColor(R.color.black_txt));
                                    llyt_take_tip3.setOnClickListener(IDCardFragment.this);
                                }

                            } else {
                                iv_take_photo3.setBackgroundDrawable(null);
                                iv_take_photo3.setImageDrawable(null);
                                iv_take_photo3.setBackgroundDrawable(drawable);

                                et_idcard.setClickable(true);
                                et_real_name.setClickable(true);

                            }

                            progressDialog.dismiss();
                            ToastUtils.showShort(R.string.upload_succeed);

                            if (step > 1 || isUploadPhotoFinish) {
                                isUploadPhotoFinish = true;
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userId", App.loginUserInfo.getUserId() + "");
                                params.put("timestamp", timestamp + "");
                                CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LIST, DsApi.GETICR), getRespListener(), params);
                                executeRequest(request);
                            }

                        }
                    });

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn_submit)
    public void submitClick() {
        RespListener respListener = new RespListener();
        respListener.onRespError = this;
        respListener.onRespSuccess = new RespListener.OnRespSuccess() {
            @Override
            public void onSuccess(String response, String extras) {
                dismiss();
                if (response == null)
                    return;
                isFinish = true;
                ToastUtils.showShort(R.string.upload_succeed);
                //next step
                if (getActivity() instanceof InfoSupplementaryActivity) {
                    InfoSupplementaryActivity supplementaryActivity = (InfoSupplementaryActivity) getActivity();
                    supplementaryActivity.rlytLxr.performClick();
                }
            }
        };

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", App.loginUserInfo.getUserId() + "");
        params.put("name", et_real_name.getText().toString());
        params.put("idNumber", et_idcard.getText().toString());
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, String.format(DsApi.LIST, DsApi.CHECKICR), respListener, params);
        executeRequest(request);
    }

    @Override
    public void onSuccess(String response, String extras) {
        super.onSuccess(response, extras);
        JSONObject jsonResp = null;
        try {
            jsonResp = new JSONObject(response);
            String name = jsonResp.optString("name");
            String idNumber = jsonResp.optString("idNumber");
            llyt_user_info.setVisibility(View.VISIBLE);
            et_real_name.setText(name);
            et_idcard.setText(idNumber);
        } catch (JSONException e) {
        }

    }

    @Override
    public void onClick(View v) {
        JSONObject idCardlJSON = App.allstatusMap.get("idcard");
        if (idCardlJSON != null && (idCardlJSON.optInt("status") == 1 || idCardlJSON.optInt("status") == 2 || idCardlJSON.optInt("status") == 3))
            return;

        if (v.getId() == R.id.llyt_take_tip1) {
            step = 0;
        } else if (v.getId() == R.id.llyt_take_tip2) {
            step = 1;
        } else {
            step = 2;
        }

        App.ossService.setGlobalDefaultStsTokenGetter(new StsTokenGetter() {
            @Override
            public OSSFederationToken getFederationToken() {
                //判断token是否过期,实效为一小时
                long timeMillis = new Date().getTime();
                long expiration = PreferenceUtils.getPrefLong(App.getContext(), App.EXPIRATION, 0);
                FederationToken token = null;
                if ((timeMillis - expiration) > 2700 * 100) {
                    // 为指定的用户拿取服务其授权需求的FederationToken
                    token = FederationTokenGetter.getToken(App.loginUserInfo.getUserId());
                    if (token == null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(R.string.get_token_failed);
                            }
                        });
                        return null;
                    }
                } else {
                    long expirationTime = PreferenceUtils.getPrefLong(App.getContext(), App.EXPIRATION, 0) / 1000;
                    token = new FederationToken(PreferenceUtils.getPrefString(App.getContext(), App.AK, ""), PreferenceUtils.getPrefString(App.getContext(), App.SK, ""), PreferenceUtils.getPrefString(App.getContext(), App.SECURITYTOKEN, ""), expirationTime);
                }
                return new OSSFederationToken(token.getAccessKeyId(), token.getAccessKeySecret(), token.getSecurityToken(), token.getExpiration());
                // 将FederationToken设置到OSSService中
            }
        });


        Intent intent = new Intent(getActivity(), CameraActivity.class);//action is
        intent.putExtra(CameraActivity.IDCARD_TYPE, step);
        startActivityForResult(intent, ConstantUtils.TAKE_PHOTO_CODE);

    }
}
