package com.kevin.recyclerviewtest.api;

import com.kevin.recyclerviewtest.bean.ArticleBean;
import com.kevin.recyclerviewtest.bean.BaseBean;
import com.kevin.recyclerviewtest.bean.PageListDataBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kevin Jern on 2018/6/29 13:07.
 */
public interface ApiServer {
    @GET(UrlContainer.HOME_LIST)
    Call<BaseBean<PageListDataBean<ArticleBean>>> getHomeList(@Path("page") int page);


}
