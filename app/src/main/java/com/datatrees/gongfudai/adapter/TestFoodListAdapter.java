package com.datatrees.gongfudai.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.datatrees.gongfudai.R;
import com.datatrees.gongfudai.volley.toolbox.NetworkImageView;
import com.datatrees.gongfudai.base.SimpleBaseAdapter;
import com.datatrees.gongfudai.net.VolleyUtil;

import java.util.List;

/**
 * Created by ucmed on 2015/7/30.
 */
public class TestFoodListAdapter extends SimpleBaseAdapter<String>{


    public TestFoodListAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.list_item_test;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView urlText = holder.getView(R.id.tv_url);
        urlText.setText((String) getItem(position));
        NetworkImageView netImageView = holder.getView(R.id.network_image_view);
        netImageView.setImageUrl((String) getItem(position),
                VolleyUtil.getImageLoader());
        return convertView;
    }


}
