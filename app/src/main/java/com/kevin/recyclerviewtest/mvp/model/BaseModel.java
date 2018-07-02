package com.kevin.recyclerviewtest.mvp.model;

import com.kevin.recyclerviewtest.api.ApiServer;
import com.kevin.recyclerviewtest.api.RetrofitUtils;

/**
 * Created by Kevin Jern on 2018/7/1 17:20.
 */
public class BaseModel implements IModel {
    /**
     * 网络请求
     * @return
     */
    @Override
    public ApiServer doRequest() {
        return RetrofitUtils.getInstance().create(ApiServer.class);
    }
}
