package com.kevin.recyclerviewtest.mvp.model;

import com.kevin.recyclerviewtest.api.RxPageListObserver;
import com.kevin.recyclerviewtest.bean.ArticleBean;

/**
 * Created by Kevin Jern on 2018/7/1 17:17.
 */
public interface IHomeModel {
    /**
     * 得到首页数据
     */
    void getHomeData(int page, RxPageListObserver<ArticleBean> rxPageListObserver);
}
