package com.kevin.recyclerviewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Kevin on 2018/3/14.
 * Recycler万能的Adapter
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "CommonRecyclerAdapter";
    //定义为protected主要是方便子类使用
    protected Context mContext;
    protected LayoutInflater mInflater;
    //数九不确定，使用泛型来传递
    protected List<T> mData;
    //布局的Id直接传到参数中
    private int mLayoutId;
    private MultiTypeSupport mTypeSupport;

    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mItemLongClickListener;

    public CommonRecyclerAdapter(Context context, List<T> data, MultiTypeSupport typeSupport) {
        this(context, data, -1);
        this.mTypeSupport = typeSupport;
    }

    public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
        this.mLayoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mTypeSupport != null) {
            //需要多布局
            mLayoutId = viewType;
        }
        //inflate出View
        View view = mInflater.inflate(mLayoutId, parent, false);
        //返回ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemLongClick(position);
                }
            });
        }
        //绑定数据这里不固定，需要传递给调用者使用
        convert(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mTypeSupport != null) {
            Log.e(TAG, "getItemViewType: " + mTypeSupport.getLayoutId(mData.get(position), position));
            return mTypeSupport.getLayoutId(mData.get(position), position);
        }
        return super.getItemViewType(position);
    }

    /**
     * 绑定数据这里不固定，需要传递给调用者使用
     *
     * @param holder
     * @param item
     */
    public abstract void convert(ViewHolder holder, T item);

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }

}
