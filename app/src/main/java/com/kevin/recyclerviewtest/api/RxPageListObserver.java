package com.kevin.recyclerviewtest.api;

import com.kevin.recyclerviewtest.bean.BaseBean;
import com.kevin.recyclerviewtest.bean.PageListDataBean;
import com.kevin.recyclerviewtest.mvp.presenter.BasePresenter;
import com.kevin.recyclerviewtest.mvp.view.IListData;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Kevin Jern on 2018/7/1 19:30.
 */
public abstract class RxPageListObserver<T> extends DisposableObserver<BaseBean<PageListDataBean<T>>> {
    private IListData<T> mListDataView;

    public RxPageListObserver(BasePresenter mPresenter) {
        this.mListDataView = (IListData<T>) mPresenter.getView();
    }

    @Override
    public void onNext(BaseBean<PageListDataBean<T>> bean) {
        if (bean.errorCode == 0) {
            PageListDataBean<T> data = bean.data;
            if (mListDataView.getPage() == 0) {
                mListDataView.clearListData();
            }
            if (data.isOver()) {
                mListDataView.showNoMore();
            } else {
                mListDataView.autoLoadMore();
            }
            onSuccess(data.getDatas());
        } else {
            onFail(bean.errorCode, bean.errorMsg);
        }

    }

    @Override
    public void onError(Throwable e) {
        mListDataView.showError();
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(List<T> mData);

    public abstract void onFail(int errorCode, String errorMsg);
}
