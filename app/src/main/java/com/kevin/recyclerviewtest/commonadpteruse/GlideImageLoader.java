package com.kevin.recyclerviewtest.commonadpteruse;

import android.content.Context;
import android.widget.ImageView;

import com.kevin.recyclerviewtest.GlideApp;
import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.ViewHolder;

/**
 * Created by Kevin on 2018/3/14.
 */

public class GlideImageLoader extends ViewHolder.HolderImageLoader {

    public GlideImageLoader(String imagePath) {
        super(imagePath);
    }

    @Override
    public void displayImage(Context context, ImageView imageView, String imagePath) {
//        Glide.with(context).load(imagePath).into(imageView);
        GlideApp.with(context)
                .load(imagePath)
                .placeholder(R.drawable.ic_discovery_default_channel)
                .centerCrop()
                .into(imageView);
    }
}
