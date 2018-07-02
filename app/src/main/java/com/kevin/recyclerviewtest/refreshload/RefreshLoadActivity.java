package com.kevin.recyclerviewtest.refreshload;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.CommonRecyclerAdapter;
import com.kevin.recyclerviewtest.adapter.OnItemClickListener;
import com.kevin.recyclerviewtest.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RefreshLoadActivity extends AppCompatActivity implements RefreshRecyclerView.OnRefreshListener, RefreshLoadRecyclerView.OnLoadMoreListener {
    private RefreshLoadRecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_load);
        mDatas = new ArrayList<>();
        initData();
        mRecyclerView = findViewById(R.id.refresh_view);
        mRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerView.addLoadViewCreator(new DefaultLoadCreator());
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);

        // 设置正在获取数据页面和无数据页面
        mRecyclerView.addLoadingView(findViewById(R.id.load_view));
        mRecyclerView.addEmptyView(findViewById(R.id.empty_view));

        mGridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
//        mRecyclerView.addItemDecoration(new GridLayoutItemDecoration(this, R.drawable.category_list_divider));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new HomeAdapter(RefreshLoadActivity.this, mDatas);
                mRecyclerView.setAdapter(mAdapter);
            }
        },3000);

        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int positon) {
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void initData() {
        for (int i = 'A'; i <= 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_action_gridview:
                mRecyclerView.setLayoutManager(mGridLayoutManager);
                break;
            case R.id.id_action_listview:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.onStopRefresh();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                mRecyclerView.onStopLoad();
                mAdapter.notifyDataSetChanged();
            }
        },2000);
    }

    class HomeAdapter extends CommonRecyclerAdapter<String> {

        public HomeAdapter(Context context, List<String> data) {
            super(context, data, R.layout.item_text);
        }

        @Override
        public void convert(ViewHolder holder, String item) {
            holder.setText(R.id.tv_text, item);
        }
    }
}
