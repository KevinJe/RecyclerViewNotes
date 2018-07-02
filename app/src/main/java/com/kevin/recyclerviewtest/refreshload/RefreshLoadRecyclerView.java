package com.kevin.recyclerviewtest.refreshload;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kevin on 2018/3/19.
 * 下拉刷新以及上拉加载更多的RecyclerView
 */

public class RefreshLoadRecyclerView extends RefreshRecyclerView {
    //上拉加载更多的辅助类
    private LoadViewCreator mLoadViewCreator;
    //上拉加载更多的View
    private View mLoadView;
    //上拉加载更多的View的高度
    private int mLoadViewHeight = 0;
    //手指按下的Y坐标
    private int mFingerDownY;
    //当前是否正在拖曳
    private boolean mCurrentDrag = false;
    //当前加载的状态
    private int mCurrentLoadStatus;
    //默认状态
    public static final int LOAD_STATUS_NORMAL = 0x0011;
    //上拉加载更多
    public static final int LOAD_STATUS_PULL_DOWN_REFRESH = 0x002;
    //松开上拉加载更多
    public static final int LOAD_STATUS_LOOSE_REFRESH = 0x0033;
    //正在加载中
    public static final int LOAD_STATUS_LOADING = 0x0044;

    public RefreshLoadRecyclerView(Context context) {
        super(context);
    }

    public RefreshLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addLoadViewCreator(LoadViewCreator creator) {
        this.mLoadViewCreator = creator;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addLoadView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreLoadView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前加载更多状态
     */
    private void restoreLoadView() {
        int currentBottomMargin = ((MarginLayoutParams) (mLoadView.getLayoutParams())).bottomMargin;
        int finalBottomMargin = 0;
        if (mCurrentLoadStatus == LOAD_STATUS_LOOSE_REFRESH) {
            mCurrentLoadStatus = LOAD_STATUS_LOADING;

            if (mLoadViewCreator != null) {
                mLoadViewCreator.onLoading();
            }
            if (mListener != null) {
                mListener.onLoad();
            }
        }
        int distance = currentBottomMargin - finalBottomMargin;
        ValueAnimator animator = ObjectAnimator.ofFloat(currentBottomMargin, finalBottomMargin)
                .setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentBottomMargin = (float) animation.getAnimatedValue();
                setLoadViewMarginBottom((int) currentBottomMargin);
            }
        });
        animator.start();
        mCurrentDrag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 如果是在最底部才处理，否则不需要处理
                if (canScrollDown(this, 1) || mLoadViewCreator == null
                        || mLoadView == null || mCurrentLoadStatus == LOAD_STATUS_LOADING) {
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e);
                }
                if (mLoadView != null) {
                    mLoadViewHeight = mLoadView.getMeasuredHeight();
                }
                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变loadView的marginBottom的值
                if (distanceY < 0) {
                    setLoadViewMarginBottom(-distanceY);
                    updateLoadStatus(-distanceY);
                    mCurrentDrag = true;
                    return false;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新加载的状态
     *
     * @param distanceY
     */
    private void updateLoadStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        } else if (distanceY < mLoadViewHeight) {
            mCurrentLoadStatus = LOAD_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentLoadStatus = LOAD_STATUS_LOOSE_REFRESH;
        }
        if (mLoadViewCreator != null) {
            mLoadViewCreator.onPull(distanceY, mLoadViewHeight, mCurrentLoadStatus);
        }
    }

    /**
     * 设置下拉加载更多的View的MarginBottom
     *
     * @param marginBottom
     */
    private void setLoadViewMarginBottom(int marginBottom) {
        MarginLayoutParams params = (MarginLayoutParams) mLoadView.getLayoutParams();
        if (marginBottom < 0) {
            marginBottom = 0;
        }
        params.bottomMargin = marginBottom;
        mLoadView.setLayoutParams(params);
    }

    /**
     * 添加底部加载更多的View
     */
    private void addLoadView() {
        Adapter adapter = getAdapter();
        if (adapter != null && mLoadViewCreator != null) {
            // 添加底部加载更多View
            View loadView = mLoadViewCreator.getLoadView(getContext(), this);
            if (loadView != null) {
                addFooterView(loadView);
                this.mLoadView = loadView;
            }
        }
    }

    /**
     * Check if this view can be scrolled vertically in a certain direction.
     * 负数（Negative）为向上滚动，正数（positive）向下滚动
     *
     * @param view      The View against which to invoke the method.
     * @param direction Negative to check scrolling up, positive to check scrolling down.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    public boolean canScrollDown(View view, int direction) {
        return view.canScrollVertically(direction);
        //这个方法过时了，用上一个
//        return ViewCompat.canScrollVertically(this, 1);
    }

    /**
     * 停止加载更多
     */
    public void onStopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        restoreLoadView();
        if (mLoadViewCreator != null) {
            mLoadViewCreator.onStopLoad();
        }
    }

    // 处理加载更多回调监听
    private OnLoadMoreListener mListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoad();
    }
}
