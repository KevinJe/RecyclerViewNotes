package com.kevin.recyclerviewtest.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.MyViewHolder;

/**
 * Created by Kevin Jern on 2018/6/29 21:12.
 */
public class CusRecyclerView extends RecyclerView {
    private static final String TAG = "CusRecyclerView";
    // 头部View
    private View mHeaderView;
    // normal
    private static final int VIEW_TYPE_NORMAL = 0;
    // header
    private static final int VIEW_TYPE_HEADER = 100;
    // footer
    private static final int VIEW_TYPE_FOOTER = 200;
    //是否允许加载更多
    private boolean isCanLoadMore;
    // 是否允许点击重新加载
    private boolean isCanReLoadMore;
    // 底部View布局I
    private int footerResId;
    // BaseAdapter
    private BaseAdapter mAdapter;
    // 底部加载监听
    private OnFooterAutoLoadMoreListner mListner;


    public CusRecyclerView(Context context) {
        this(context, null);
    }

    public CusRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //添加监听
        addOnScrollListener(mScrollListener);
    }

    /**
     * 添加头部View
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    /**
     * 移除头部View
     */
    public void removeHeaderView() {
        mHeaderView = null;
    }

    /**
     * 添加底部View
     *
     * @param footerResId
     */
    public void showFooterStatus(int footerResId) {
        this.footerResId = footerResId;
    }

    /**
     * 设置是否可以加载更多
     *
     * @param isCanLoadMore
     */
    public void setCanLoadMore(boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;
    }

    /**
     * 设置是否可以点击重新加载更多
     *
     * @param isCanReLoadMore
     */
    public void setCanReLoadMore(boolean isCanReLoadMore) {
        this.isCanReLoadMore = isCanReLoadMore;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAdapter = new BaseAdapter(adapter);
        }
        Log.e(TAG, "setAdapter: adapter");
        super.swapAdapter(mAdapter, true);
    }

    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        /**
         *  滑动到底部的监听
         * @param recyclerView
         * @param dx
         * @param dy
         */
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int lastChildPosition = linearLayoutManager.findLastVisibleItemPosition();
                int totalCount = linearLayoutManager.getItemCount();
                View lastChildView = linearLayoutManager.getChildAt(linearLayoutManager.getChildCount() - 1);
                int lastChildViewBottom = lastChildView.getBottom();
                int recyclerBottom = getBottom();
                Log.e(TAG, "onScrolled: " + (lastChildPosition == totalCount - 1));
                Log.e(TAG, "onScrolled: lastChildPosition " + lastChildPosition);
                Log.e(TAG, "onScrolled: totalCount " + totalCount);
                if (lastChildPosition == totalCount - 1 && lastChildViewBottom == recyclerBottom) {
                    if (isCanLoadMore && mListner != null) {
                        // 加载到底部加载更多
                        mListner.loadMore();
                        Log.e(TAG, "onScrolled: 加载到底部");
                    }
                }
            }

        }
    };


    private class BaseAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private Adapter mAdapter;

        public BaseAdapter(Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public int getItemViewType(int position) {
            if (mHeaderView != null && position == 0) {
                return VIEW_TYPE_HEADER;
            }
            if (isCanLoadMore && position == getItemCount() - 1) {
                return VIEW_TYPE_FOOTER;
            }
            return VIEW_TYPE_NORMAL;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                return MyViewHolder.createViewHolder(mHeaderView);
            }
            if (viewType == VIEW_TYPE_FOOTER) {
                Log.e(TAG, "onCreateViewHolder: 创建了" );
                return MyViewHolder.createViewHolder(parent, R.layout.item_root_footer);
            }
            return (MyViewHolder) mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_NORMAL) {
                if (mHeaderView != null) {
                    position--;
                }
                mAdapter.onBindViewHolder(holder, position);
            } else if (viewType == VIEW_TYPE_FOOTER) {
                showFooterView(holder);
            }

        }

        @Override
        public int getItemCount() {
            int count = mAdapter.getItemCount();
            if (mHeaderView != null) {
                count++;
            }
            if (isCanLoadMore) {
                count++;
            }
            return count;
        }
    }

    /**
     * 显示footer
     *
     * @param holder
     */
    private void showFooterView(MyViewHolder holder) {
        FrameLayout footerView = holder.getView(R.id.root_footer);
        footerView.removeAllViews();
        if (footerResId != 0) {
            View view = LayoutInflater.from(getContext())
                    .inflate(footerResId, footerView, false);
            footerView.addView(view);
            // 加载失败，重新加载
            footerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isCanReLoadMore) {
                        return;
                    }
                    if (mListner != null) {
                        showLoadMore();
                        mListner.reLoadMore();
                    }
                }
            });
        }
    }

    /**
     * 加载更多
     */
    public void showLoadMore() {
        showFooterStatus(R.layout.item_footer_loading_more);
        setCanReLoadMore(false);
    }

    /**
     * 没有更多数据了
     */
    public void showNoMoreData() {
        showFooterStatus(R.layout.item_footer_loading_nomore);
        setCanReLoadMore(false);
    }

    /**
     * 加载出错
     */
    public void showLoadMoreError() {
        showFooterStatus(R.layout.item_footer_loading_error);
        setCanReLoadMore(true);
    }

    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
    }

    public void notifyItemRemoved(int position) {
        getAdapter().notifyItemRemoved(position);
    }

    public void addFooterAutoLoadMoreListner(OnFooterAutoLoadMoreListner listner) {
        this.mListner = listner;
    }


    public interface OnFooterAutoLoadMoreListner {
        // 自动加载更多
        void loadMore();

        //加载出错，点击重新加载
        void reLoadMore();
    }
}
