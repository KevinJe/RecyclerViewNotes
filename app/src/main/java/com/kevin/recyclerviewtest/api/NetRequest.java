package com.kevin.recyclerviewtest.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kevin Jern on 2018/6/29 22:22.
 */
public class NetRequest {
    private static final String URL = "http://www.wanandroid.com/";
    private ApiServer mServer;
    private  Retrofit mRetrofit;

    public ApiServer get() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit
                .Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return mRetrofit.create(ApiServer.class);
    }

}
