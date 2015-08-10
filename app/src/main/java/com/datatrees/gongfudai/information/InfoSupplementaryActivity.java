package com.datatrees.gongfudai.information;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.base.BaseFragmentActivity;
import com.datatrees.gongfudai.utils.BK;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 信息补全主界面
 * Created by zhangping on 15/8/10.
 */
public class InfoSupplementaryActivity extends BaseFragmentActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ibtn_operation)
    ImageButton ibtnOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_supplementary);
        BK.bind(this);
        ibtnOperation.setVisibility(View.INVISIBLE);
        tvTitle.setText(R.string.info_sup);
    }

    @OnClick(R.id.ibtn_back)
    public void goBack() {
        this.finish();
    }
}
