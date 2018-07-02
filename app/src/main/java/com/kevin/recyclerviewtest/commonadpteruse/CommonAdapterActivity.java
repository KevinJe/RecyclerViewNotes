package com.kevin.recyclerviewtest.commonadpteruse;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.api.ApiServer;
import com.kevin.recyclerviewtest.api.RetrofitUtils;
import com.kevin.recyclerviewtest.bean.ArticleBean;
import com.kevin.recyclerviewtest.bean.BaseBean;
import com.kevin.recyclerviewtest.bean.PageListDataBean;
import com.kevin.recyclerviewtest.widget.recyclerview.CusRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommonAdapterActivity extends AppCompatActivity implements CusRecyclerView.OnFooterAutoLoadMoreListner {
    private static final String TAG = "CommonAdapterActivity";
    private CusRecyclerView mRecyclerView;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler = new Handler();
    private List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> mData;
    //    private CategoryListAdapter mAdapter;
    private List<ArticleBean> mDatas;
    private ArticleAdapter mAdapter;
    private int page;
    private ApiServer mServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new CategoryItemDecoration(this, R.drawable.category_list_divider));
        mOkHttpClient = new OkHttpClient();
//        requestListData();
        mDatas = new ArrayList<>();
        mRecyclerView.setCanLoadMore(true);
        mRecyclerView.addFooterAutoLoadMoreListner(this);
        requestData();
    }


    private void requestData() {
//        final NetRequest request = new NetRequest();
//        mServer = request.get();
        RetrofitUtils instance = RetrofitUtils.getInstance();
        mServer = instance.create(ApiServer.class);
        /*Observable<BaseBean<PageListDataBean<ArticleBean>>> observable = server.getHomeList(0);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseBean<PageListDataBean<ArticleBean>>>() {
            @Override
            public void accept(BaseBean<PageListDataBean<ArticleBean>> bean) throws Exception {
                mDatas.addAll(bean.data.getDatas());
                mRecyclerView.setAdapter(new ArticleAdapter(mDatas));
            }
        });*/
        retrofit2.Call<BaseBean<PageListDataBean<ArticleBean>>> call = mServer.getHomeList(page);
        call.enqueue(new retrofit2.Callback<BaseBean<PageListDataBean<ArticleBean>>>() {

            @Override
            public void onResponse(retrofit2.Call<BaseBean<PageListDataBean<ArticleBean>>> call, retrofit2.Response<BaseBean<PageListDataBean<ArticleBean>>> response) {
                mDatas.addAll(response.body().data.getDatas());
                if (mAdapter == null) {
                    mAdapter = new ArticleAdapter();
                }
                if (page == 0) {
                    mRecyclerView.setAdapter(mAdapter);
                }
                mAdapter.notifyAllDatas(mDatas, mRecyclerView);
                mRecyclerView.showLoadMore();
                page++;
            }

            @Override
            public void onFailure(retrofit2.Call<BaseBean<PageListDataBean<ArticleBean>>> call, Throwable t) {
                mRecyclerView.showLoadMoreError();
            }
        });
    }

    /**
     * 请求列表数据，内涵段子
     */
    private void requestListData() {
        Request.Builder builder = new Request.Builder();
        final Request request = builder.url("http://is.snssdk.com/2/essay/discovery/v3/?iid=6152551759&channel=360&aid=7" +
                "&app_name=joke_essay&version_name=5.7.0&ac=wifi&device_id=30036118478&device_brand=Xiaomi&update_version_code=5701&" +
                "manifest_version_code=570&longitude=113.000366&latitude=28.171377&device_platform=android").build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到json数据
                String result = response.body().string();
                //gson解析成对象
                ChannelListResult channelListResult = new Gson().fromJson(result, ChannelListResult.class);
                //获取列表数据
                final List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> categoryList =
                        channelListResult.getData().getCategories().getCategory_list();
                //这个回调的方法不在主线程，利用Handler到主线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showListData(categoryList);
                    }
                });
            }
        });

    }

    private void showListData(List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> categoryList) {
        mData = categoryList;
//        mAdapter = new CategoryListAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
       /* mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int positon) {
                Toast.makeText(CommonAdapterActivity.this, mData.get(positon).getName() + "  " + positon, Toast.LENGTH_SHORT).show();
                mData.remove(positon);
                mAdapter.notifyItemRemoved(positon);
            }
        });*/
        /*//长按事件
        mAdapter.setOnItemLongClickListener(new CategoryListAdapter.ItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(CommonAdapterActivity.this, "长按了   " + mData.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void loadMore() {
        requestData();
    }

    @Override
    public void reLoadMore() {
        requestData();
    }

}
