package com.kevin.recyclerviewtest.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kevin Jern on 2018/7/1 16:37.
 *  网络请求
 */
public class RetrofitUtils {
    private Retrofit mRetrofit;

    private RetrofitUtils() {
        initRetrofit();
    }

    public static RetrofitUtils getInstance() {
        return RetrofitHolder.instance;
    }

    private static class RetrofitHolder {
        private static final RetrofitUtils instance = new RetrofitUtils();
    }

    private void initRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit
                .Builder()
                .baseUrl(UrlContainer.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 返回接口对应的类
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> cls) {
        return mRetrofit.create(cls);
    }
}
