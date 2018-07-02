package com.kevin.recyclerviewtest.refreshload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.kevin.recyclerviewtest.R;

/**
 * Created by Kevin on 2018/3/18.
 * 下拉刷新的实现类
 */

public class DefaultRefreshCreator extends RefreshViewCreator {
    // 加载数据的ImageView
    private View mRefreshView;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        mRefreshView = LayoutInflater.from(context)
                .inflate(R.layout.refresh_header_view, parent, false);
//        mRefreshView = view.findViewById(R.id.refresh_iv);
        return mRefreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        // 不断下拉的过程中旋转图片
        mRefreshView.setRotation(rotate * 360);
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setDuration(1000);
        mRefreshView.setAnimation(rotateAnimation);
    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshView.setRotation(0);
        mRefreshView.clearAnimation();
    }
}
