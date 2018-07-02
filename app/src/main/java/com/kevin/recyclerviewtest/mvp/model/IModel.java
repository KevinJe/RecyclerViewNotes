package com.kevin.recyclerviewtest.mvp.model;

import com.kevin.recyclerviewtest.api.ApiServer;

/**
 * Created by Kevin Jern on 2018/7/1 17:03.
 */
public interface IModel {
    /**
     * 网络请求
     *
     * @return
     */
    ApiServer doRequest();
}
