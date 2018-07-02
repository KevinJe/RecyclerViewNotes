package com.kevin.recyclerviewtest.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kevin.recyclerviewtest.adapter.OnItemClickListener;
import com.kevin.recyclerviewtest.adapter.OnItemLongClickListener;

/**
 * Created by Kevin on 2018/3/15.
 */

public class WrapRecyclerView extends RecyclerView {
    private static final String TAG = "WrapRecyclerView";
    // 包裹了一层的头部底部Adapter
    private WrapRecyclerViewAdapter mWrapRecyclerAdapter;
    // 这个是列表数据的Adapter
    private RecyclerView.Adapter mAdapter;
    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    private View mEmptyView, mLoadingView;
    private boolean hasHeaderView = false;

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        // 为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
            mAdapter = null;
        }
        this.mAdapter = adapter;
        //从这里开始左包裹类，如果是WrapRecyclerViewAdapter，就直接用
        //否则new WrapRecyclerViewAdapter(adapter);将adpter包裹进去
        //这样处理后显得更优雅
        if (adapter instanceof WrapRecyclerViewAdapter) {
            mWrapRecyclerAdapter = (WrapRecyclerViewAdapter) adapter;
            Log.e(TAG, "setAdapter: (WrapRecyclerViewAdapter) adapter");
        } else {
            mWrapRecyclerAdapter = new WrapRecyclerViewAdapter(adapter);
            Log.e(TAG, "setAdapter: new WrapRecyclerViewAdapter");
        }
        //这里必须传入mWrapRecyclerAdapter，用来返回新的带包裹的Adapter
        super.setAdapter(mWrapRecyclerAdapter);
        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(this);
        //注册一个观察者模式
        mAdapter.registerAdapterDataObserver(mObserver);
        // 加载数据页面
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(GONE);
        }
    }

    /**
     * RecyclerView添加头部View
     *
     * @param view
     */
    public void addHeaderView(View view) {
        // 如果没有Adapter那么就不添加，也可以选择抛异常提示
        // 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
            hasHeaderView = true;
        }
    }

    /**
     * 移除头部的View
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeHeaderView(view);
        }
    }

    /**
     * RecyclerView添加底部View
     *
     * @param view
     */
    public void addFooterView(View view) {
        // 如果没有Adapter那么就不添加，也可以选择抛异常提示
        // 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addFooterView(view);
        }
    }

    /**
     * 移除底部的View
     *
     * @param view
     */
    public void removeFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeFooterView(view);
        }
    }

    /**
     * 界面为空需要显示的View
     */
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    private void dataChanged() {
        if (mAdapter.getItemCount() == 0) {
            //没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
            } else {
                mEmptyView.setVisibility(GONE);
            }
        }
    }

    /**
     * 加载数据时需要显示的View
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
        mLoadingView.setVisibility(VISIBLE);
    }

    /**
     * RecyclerView的数据观察者，通过他来及时的更新数据
     */
    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) {
                return;
            }
            if (mWrapRecyclerAdapter != mAdapter) {
                // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);
            }
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) {
                return;
            }
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart, payload);
            }
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }
            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRangeRemoved(positionStart, itemCount);
                Log.e(TAG, "onItemRangeRemoved: ");
            }
            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            }
            dataChanged();
        }
    };
    /**
     * 点击事件以及长按事件
     */
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.setOnItemClickListener(mItemClickListener);
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongClickListener = listener;
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.setOnItemLongClickListener(mLongClickListener);
        }
    }
}
