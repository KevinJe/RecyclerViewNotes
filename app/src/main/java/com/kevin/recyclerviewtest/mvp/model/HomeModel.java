package com.kevin.recyclerviewtest.mvp.model;

import com.kevin.recyclerviewtest.api.RxPageListObserver;
import com.kevin.recyclerviewtest.bean.ArticleBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kevin Jern on 2018/7/1 17:20.
 */
public class HomeModel extends CommonModel implements IHomeModel {

    @Override
    public void getHomeData(int page, RxPageListObserver<ArticleBean> rxPageListObserver) {
        doRequest()
                .getHomeList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxPageListObserver);
    }
}
