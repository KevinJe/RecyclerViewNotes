package com.kevin.recyclerviewtest.wrap;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kevin.library.BannerAdapter;
import com.kevin.library.BannerView;
import com.kevin.recyclerviewtest.GlideApp;
import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.OnItemClickListener;
import com.kevin.recyclerviewtest.commonadpteruse.CategoryItemDecoration;
import com.kevin.recyclerviewtest.commonadpteruse.CategoryListAdapter;
import com.kevin.recyclerviewtest.commonadpteruse.ChannelListResult;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderFooterListActivity extends AppCompatActivity {
    private static final String TAG = "HeaderFooterListActivit";
    //自定义的RecyclerView，增加了添加头部以及底部的功能
    private WrapRecyclerView mRecyclerView;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler = new Handler();
    private List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> mData;
    private CategoryListAdapter mAdapter;
    private boolean isHeader = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CategoryItemDecoration(this, R.drawable.category_list_divider));
        Log.e(TAG, "onCreate: ");
        mOkHttpClient = new OkHttpClient();
        requestListData();
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
                final ChannelListResult channelListResult = new Gson().fromJson(result, ChannelListResult.class);
                //获取列表数据
                final List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> categoryList =
                        channelListResult.getData().getCategories().getCategory_list();
                //这个回调的方法不在主线程，利用Handler到主线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showListData(categoryList);
                        addBannerView(channelListResult.getData().getRotate_banner().getBanners());
                    }
                });
            }
        });

    }

    private void addBannerView(final List<ChannelListResult.DataBean.BannerBean> banners) {
        BannerView banenrView = (BannerView) LayoutInflater.from(this)
                .inflate(R.layout.banner_view, mRecyclerView, false);
        banenrView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView imageView = null;
                //只有在convertView不为空的情况下才进行复用，否则新建ImageView
                if (convertView == null) {
                    imageView = new ImageView(HeaderFooterListActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView = (ImageView) convertView;
                    Log.e(TAG, "getView: 界面复用" + convertView);
                }
                GlideApp.with(HeaderFooterListActivity.this)
                        .load(banners.get(position).banner_url
                                .url_list.get(0).url).into(imageView);

                return imageView;
            }

            @Override
            public int getCount() {
                return banners.size();
            }

            @Override
            public String getBannerDesc(int position) {
                return banners.get(position).banner_url.title;
            }
        });
        //开始轮播
        banenrView.startScroll();
        mRecyclerView.addHeaderView(banenrView);
        isHeader = true;
    }

    private void showListData(List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> categoryList) {
        mData = categoryList;
        mAdapter = new CategoryListAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        //使用包装过的Adapter来包装Adapter，从而使其增加添加头部以及底部的功能
//        final WrapRecyclerViewAdapter wrapAdapter = new WrapRecyclerViewAdapter(mAdapter);
//        mRecyclerView.setAdapter(mAdapter);
//        final View view = LayoutInflater
//                .from(this).inflate(R.layout.layout_header_footer, mRecyclerView, false);
//        mRecyclerView.addHeaderView(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRecyclerView.removeHeaderView(view);
//            }
//        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int positon) {
                Log.d(TAG, "onItemClick: " + positon);
                int realPosition = getDataRealPosition(positon);
                Toast.makeText(HeaderFooterListActivity.this, "" + mData.get(realPosition).getName() + "　　" + realPosition, Toast.LENGTH_SHORT).show();
                mData.remove(realPosition);
                // TODO: 2018/4/6 下面这个位置传0会报错，上面的不会
                mAdapter.notifyItemRemoved(positon);
            }
        });
    }

    /**
     * 由于添加了头部，所以第一条数据的位置在mData中为0
     * 但是mAdapter由于被包裹为wrapRecyclerViewAadpter，所以在他看来第一条数据位置为1
     * 所以，在这里返回减去头部的位置
     * @param positon
     */
    private int getDataRealPosition(int positon) {
        return positon - 1;
    }
}
