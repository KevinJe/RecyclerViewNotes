package com.kevin.recyclerviewtest.refreshload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.kevin.recyclerviewtest.R;

/**
 * Created by Kevin on 2018/3/19.
 * 上拉加载的实现类
 */

public class DefaultLoadCreator extends LoadViewCreator {
    // 加载数据的ImageView
    private TextView mLoadTv;
    private View mRefreshIv;

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View loadView = LayoutInflater.from(context)
                .inflate(R.layout.load_footer_view, parent, false);
        mLoadTv = loadView.findViewById(R.id.load_tv);
        mRefreshIv = loadView.findViewById(R.id.refresh_iv);
        return loadView;
    }

    @Override
    public void onPull(int currentDragHeight, int loadViewHeight, int currentLoadStatus) {
        if (currentLoadStatus == RefreshLoadRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mLoadTv.setText("上拉加载更多");
        }
        if (currentLoadStatus == RefreshLoadRecyclerView.LOAD_STATUS_LOOSE_REFRESH) {
            mLoadTv.setText("松开加载更多");
        }
    }

    @Override
    public void onLoading() {
        mLoadTv.setVisibility(View.INVISIBLE);
        mRefreshIv.setVisibility(View.VISIBLE);

        // 加载的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopLoad() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
        mLoadTv.setText("上拉加载更多");
        mLoadTv.setVisibility(View.VISIBLE);
        mRefreshIv.setVisibility(View.INVISIBLE);
    }
}
