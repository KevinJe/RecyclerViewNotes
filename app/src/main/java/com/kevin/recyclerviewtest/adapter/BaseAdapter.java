package com.kevin.recyclerviewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kevin.recyclerviewtest.widget.recyclerview.CusRecyclerView;

import java.util.List;

/**
 * Created by Kevin Jern on 2018/6/29 21:14.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "BaseAdapter";
    // Context
    private Context mContext;
    // LayoutInflater
    private LayoutInflater mInflater;
    // data
    protected List<T> mDatas;

    //刷新所有数据
    public void notifyAllDatas(List<T> mList, CusRecyclerView recyclerView) {
        this.mDatas = mList;
        recyclerView.notifyDataSetChanged();
    }


    /*public BaseAdapter(Context context) {
        this.mContext = context;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MyViewHolder.createViewHolder(parent, getLayoutId(viewType));
    }

    // item的布局
    protected abstract int getLayoutId(int viewType);

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        T t = mDatas.get(position);
        bindData(holder, t, position);
    }

    protected abstract void bindData(MyViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }
}
