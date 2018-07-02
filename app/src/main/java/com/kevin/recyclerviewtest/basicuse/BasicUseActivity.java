package com.kevin.recyclerviewtest.basicuse;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BasicUseActivity extends AppCompatActivity {
    private static final String TAG = "BasicUseActivity";
    private List<String> mDatas;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initData();
        mRecyclerView = findViewById(R.id.recyclerview);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.addItemDecoration(new GridLayoutItemDecoration(this, R.drawable.item_divider));
        final MyAdapter adapter = new MyAdapter(this, mDatas);
        mRecyclerView.setAdapter(adapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int positon) {
                mDatas.remove(positon);
                adapter.notifyItemRemoved(positon);
            }
        });
    }

    protected void initData() {
        mDatas = new ArrayList<>();
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
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.id_action_listview:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<String> mDatas;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, List<String> datas) {
            mInflater = LayoutInflater.from(context);
            mDatas = datas;
        }

        /**
         * 这个是必须实现的方法，当RecyclerView需要ViewHolder来展示指定类型的item时调用
         *
         * @param parent   手动new出来的View或者inflate的View所依附的ViewGroup
         * @param viewType item的类型，可以通过getItemViewType()返回对应的type，就可以实现不同的布局
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            Log.e(TAG, "onCreateViewHolder: " + parent.equals(mRecyclerView));
            View view = mInflater.inflate(R.layout.item_text, parent, false);
            return new ViewHolder(view);
        }

        /**
         * 也是抽象的，必须实现，在这里进行数据的绑定
         *
         * @param holder   需要的展示数据使用的ViewHolder
         * @param position 当前item的位置
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            String data = mDatas.get(position);
            ViewGroup.LayoutParams params = holder.tv_text.getLayoutParams();
            params.height = (int) (Math.random() * 300 + 100);
            holder.tv_text.setLayoutParams(params);
            holder.tv_text.setText(data);
            if (mListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(position);
                    }
                });
            }
        }

        /**
         * 也是必须要实现的，得到item的总数
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_text = itemView.findViewById(R.id.tv_text);
        }
    }

    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick();
    }
}
