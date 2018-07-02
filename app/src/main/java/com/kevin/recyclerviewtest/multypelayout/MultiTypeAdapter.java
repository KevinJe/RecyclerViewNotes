package com.kevin.recyclerviewtest.multypelayout;

import android.content.Context;

import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.CommonRecyclerAdapter;
import com.kevin.recyclerviewtest.adapter.MultiTypeSupport;
import com.kevin.recyclerviewtest.adapter.ViewHolder;

import java.util.List;

/**
 * Created by Kevin Jern on 2018/5/5 21:09.
 */
public class MultiTypeAdapter extends CommonRecyclerAdapter<ChatBean> {
    private static final String TAG = "MultiTypeAdapter";

    public MultiTypeAdapter(Context context, List<ChatBean> data) {
        super(context, data, new MultiTypeSupport<ChatBean>() {
            @Override
            public int getLayoutId(ChatBean item, int position) {
//                Log.e(TAG, "getLayoutId: " + item.getType());
                if (item.getType() == 0) {
                    return R.layout.item_type_left;
                } else {
                    return R.layout.item_type_right;
                }
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, ChatBean item) {
        holder.setText(R.id.tv_multi, item.getText());
    }
}
