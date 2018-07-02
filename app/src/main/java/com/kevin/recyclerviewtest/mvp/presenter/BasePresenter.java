package com.kevin.recyclerviewtest.mvp.presenter;

import com.kevin.recyclerviewtest.mvp.view.IView;

import java.lang.ref.WeakReference;

/**
 * Created by Kevin Jern on 2018/7/1 17:04.
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {
    private WeakReference<V> weakView;

    /**
     * 检查是否AttachView
     *
     * @return
     */
    private boolean isAttachView() {
        return weakView != null && weakView.get() != null;
    }

    @Override
    public void attachView(V view) {
        this.weakView = new WeakReference<>(view);
    }

    @Override
    public void dettachView() {
        if (weakView != null) {
            weakView.clear();
            weakView = null;
        }
    }

    @Override
    public V getView() {
        return weakView.get();
    }
}
