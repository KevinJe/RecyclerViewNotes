package com.kevin.recyclerviewtest.refreshload;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kevin on 2018/3/18.
 * 下拉刷新的辅助类
 */

public abstract class RefreshViewCreator {
    /**
     * 获取下拉刷新的View
     *
     * @param context
     * @param parent  RecyclerView
     * @return
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     *
     * @param currentDragHeight    当前拖动的高度
     * @param refreshViewHeight    总的刷新高度
     * @param currentRefreshStatus 当前的状态
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus);

    /**
     * 正在刷新中
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();
}
