package com.kevin.recyclerviewtest.adapter;

/**
 * Created by Kevin on 2018/3/14.
 * 多布局
 */

public interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
    int getLayoutId(T item, int position);
}
