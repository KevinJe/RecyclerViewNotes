package com.kevin.recyclerviewtest.commonadpteruse;

import com.kevin.recyclerviewtest.api.NetCallback;
import com.kevin.recyclerviewtest.api.RxPageListObserver;
import com.kevin.recyclerviewtest.bean.ArticleBean;
import com.kevin.recyclerviewtest.mvp.model.HomeModel;
import com.kevin.recyclerviewtest.mvp.presenter.BasePresenter;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Kevin Jern on 2018/7/1 17:33.
 */
public class HomePresenter extends BasePresenter<HomeContract.IHomeView> implements HomeContract.IHomePresenter {
    private HomeModel mHomeModel;
    private HomeContract.IHomeView mHomeView;
    private CompositeDisposable mCompositeDisposable;

    public HomePresenter() {
        mHomeModel = new HomeModel();
    }


    @Override
    public void getHomeData(NetCallback callback) {
        mHomeView = getView();
        RxPageListObserver<ArticleBean> observer = new RxPageListObserver<ArticleBean>(this) {

            @Override
            public void onSuccess(List<ArticleBean> mData) {
                mHomeView.setData(mData);
                if (mHomeView.getData().size() == 0) {

                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg) {

            }
        };
        mHomeModel.getHomeData(mHomeView.getPage(), observer);
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(observer);
    }
}
