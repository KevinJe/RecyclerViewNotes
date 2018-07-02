package com.kevin.recyclerviewtest.mvp.view;

import java.util.List;

/**
 * Created by Kevin Jern on 2018/7/1 17:38.
 */
public interface IListData<T> extends IView {
    int getPage();

    void setData(List<T> data);

    List<T> getData();

    void showContent(); //显示内容

    void showError(); //加载错误

    void autoLoadMore();//自动加载

    void clearListData();//清空所有数据

    void showNoMore();//没有更多数据

    int getArticleId();//文章id
}
