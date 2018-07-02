package com.kevin.recyclerviewtest.wrap;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.kevin.recyclerviewtest.adapter.OnItemClickListener;
import com.kevin.recyclerviewtest.adapter.OnItemLongClickListener;

/**
 * Created by Kevin on 2018/3/15.
 * 为RecyclerView增加头部以及底部View，仿照ListView的书写方法
 */

public class WrapRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "WrapRecyclerViewAdapter";
    //数据列表的Adapter，不包含头部
    private RecyclerView.Adapter mAdapter;
    //头部以及底部的集合
    private SparseArray<View> mHeaders, mFooters;
    private static int BASE_HEADER_KEY = 1000000;
    private static int BASE_FOOTER_KEY = 2000000;

    public WrapRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        mHeaders = new SparseArray();
        mFooters = new SparseArray();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Header
        if (mHeaders.indexOfKey(viewType) >= 0) {
            return createHeaderFooterViewHolder(mHeaders.get(viewType));
        } else if (mFooters.indexOfKey(viewType) >= 0) {
            //Footer
            return createHeaderFooterViewHolder(mFooters.get(viewType));
        }
        //Adapter
        return mAdapter.onCreateViewHolder(parent, getItemViewType(viewType));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //是头部直接返回
        int numHeaders = mHeaders.size();
        if (position < numHeaders) {
            return;
        }
        // Adapter
        // 得到 Adapter的真实位置，因为加入有头部position为 0
        // 所以，当前的Adapter的第一条item的position为 1
        // 所以，就要减掉头部的条数，得到Adapter中item的真实位置
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
            }
        }

        //点击事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(adjPosition);
                }
            });
        }
        //长按事件
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onItemLongClick(adjPosition);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Header
        int numHeaders = mHeaders.size();
        int adapterCount = mAdapter.getItemCount();
        if (position < numHeaders) {
            return mHeaders.keyAt(position);
        }
        //Footer
        if (position >= (numHeaders + adapterCount)) {
            return mFooters.keyAt(position);
        }
        //Adapter
        int adjPosition = position - numHeaders;
        return mAdapter.getItemViewType(adjPosition);
    }

    @Override
    public int getItemCount() {
        //条数为三者相加 = Adapter的条数 + 底部条数 + 头部条数
        return mAdapter.getItemCount() + mHeaders.size() + mFooters.size();
    }

    /**
     * 创建头部以及底部的ViewHolder
     *
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {

        };
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    /**
     * 添加头部
     * 添加以及移除View时要notifyDataSetChanged及时的更新数据
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (mHeaders.indexOfValue(view) == -1) {
            mHeaders.put(BASE_HEADER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        if (mHeaders.indexOfValue(view) >= 0) {
            mHeaders.removeAt(mHeaders.indexOfValue(view));
        }
        notifyDataSetChanged();
    }

    /**
     * 添加底部
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (mFooters.indexOfValue(view) == -1) {
            mFooters.put(BASE_FOOTER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除底部
     *
     * @param view
     */
    public void removeFooterView(View view) {
        if (mFooters.indexOfValue(view) >= 0) {
            mFooters.removeAt(mFooters.indexOfValue(view));
        }
        notifyDataSetChanged();
    }

    /**
     * 是不是底部位置
     *
     * @param position
     * @return
     */
    private boolean isFooterPosition(int position) {
        return position >= (mHeaders.size() + mAdapter.getItemCount());
    }

    /**
     * 是不是头部位置
     *
     * @param position
     * @return
     */
    private boolean isHeaderPositon(int position) {
        return position < mHeaders.size();
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recyclerView
     * @version 1.0
     */
    public void adjustSpanSize(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderPositon(position)
                            || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 点击和长按事件
     */
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }
}
