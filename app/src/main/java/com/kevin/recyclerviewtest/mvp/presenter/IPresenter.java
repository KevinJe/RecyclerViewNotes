package com.kevin.recyclerviewtest.mvp.presenter;

import com.kevin.recyclerviewtest.mvp.view.IView;

/**
 * Created by Kevin Jern on 2018/7/1 17:02.
 */
public interface IPresenter<V extends IView> {
    /**
     * 绑定视图
     *
     * @param view
     */
    void attachView(V view);

    /**
     * 解除绑定（每个V记得使用完之后解绑，主要是用于防止内存泄漏问题）
     */
    void dettachView();

    V getView();

}
