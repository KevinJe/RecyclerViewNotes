package com.kevin.recyclerviewtest.refreshload;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kevin on 2018/3/19.
 * 上拉加载更多的辅助类
 */

public abstract class LoadViewCreator {
    /**
     * 获取上拉加载更多的View
     *
     * @param context
     * @param parent  RecyclerView
     * @return
     */
    public abstract View getLoadView(Context context, ViewGroup parent);

    /**
     * 正在上拉
     *
     * @param currentDragHeight 当前拖动的高度
     * @param loadViewHeight    总的加载高度
     * @param currentLoadStatus 当前状态
     */
    public abstract void onPull(int currentDragHeight, int loadViewHeight, int currentLoadStatus);

    /**
     * 正在加载中
     */
    public abstract void onLoading();

    /**
     * 停止加载
     */
    public abstract void onStopLoad();
}
