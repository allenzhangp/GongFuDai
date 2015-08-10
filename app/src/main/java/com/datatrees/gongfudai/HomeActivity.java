package com.datatrees.gongfudai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.datatrees.gongfudai.base.BaseFragmentActivity;
import com.datatrees.gongfudai.information.InfoSupplementaryActivity;
import com.datatrees.gongfudai.utils.BK;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhangping on 15/8/10.
 */
public class HomeActivity extends BaseFragmentActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Bind(R.id.rg_home)
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BK.bind(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TestFragment()).commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_sw:
                        break;
                    case R.id.rbtn_qz:
                        break;
                    case R.id.rbtn_mj:
                        break;
                }

            }
        });
    }

    @OnClick(R.id.ibtn_news)
    public void toHis() {

    }

    @OnClick(R.id.ibtn_operation)
    public void toOperation() {
        startActivity(new Intent(this, InfoSupplementaryActivity.class));
    }

}
