package com.kevin.recyclerviewtest.widget.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Jern on 2018/7/1 19:50.
 */
public class BannerPager extends ViewPager {
    // Adapter
    private BanAdapter mAdapter;
    //界面复用的View
    private List<View> mConvertViews;
    //定时执行的handler
    private Handler mHandler;
    //发送消息的msgWhat
    private final int SCROLL_MSG = 0x0011;
    // 页面切换间隔时间
    private int mCutDownTime = 3500;
    // 当前的标志位
    private Activity mActivity;
    //重新开始轮播的标志位
    private boolean reStart = true;


    public BannerPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        mConvertViews = new ArrayList<>();
        initHandler();
    }

    private void initHandler() {
        //handleMessage需要有消息时才会执行，所以如果没有消息，不会执行
        //也就是不手动调用startScroll时是不会执行自动轮播的
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //每隔一段事件切换ViewPager当前的item
                setCurrentItem(getCurrentItem() + 1);
                // 循环执行，实现轮播
                startScroll();
            }
        };
    }

    /**
     * 开始轮播
     */
    private void startScroll() {
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
    }

    public void setAdapter(BanAdapter adapter) {
        mAdapter = adapter;
        // 这里才是真正的ViewPager设置Adapter处
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * banner离开视野，停止轮播
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            mHandler.removeMessages(SCROLL_MSG);
            mHandler = null;
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(mCallbacks);
            reStart = false;
        }
        super.onDetachedFromWindow();
    }

    /**
     * banner进入视野开始轮播
     */
    @Override
    protected void onAttachedToWindow() {
        initHandler();
        if (!reStart) {
            mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
        }
        //注册生命周期
        mActivity.getApplication().registerActivityLifecycleCallbacks(mCallbacks);
        reStart = true;
        super.onAttachedToWindow();
    }

    private class BannerPagerAdapter extends PagerAdapter {
        /**
         * 实现无限轮播，返回最大值
         *
         * @return
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // 由于 getCount返回了一个极大的值,所以这里需要求余
            View view = mAdapter.getView(position % mAdapter.getCount(), getConvertView());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //移除页面
            container.removeView((View) object);
            // 将销毁的加入缓存中
            mConvertViews.add((View) object);
        }

        /**
         * 这里是处理手指快速滑动时，ViewPager的item复用会出现问题
         * 这里只在复用的item的父布局为空，才去使用该复用的View
         * 否则，会出现，这个View还没有销毁，就又再次复用这个View，这是不允许的
         *
         * @return
         */
        private View getConvertView() {
            for (int i = 0; i < mConvertViews.size(); i++) {
                if (mConvertViews.get(i).getParent() == null) {
                    return mConvertViews.get(i);
                }
            }
            return null;
        }
    }

    private DefaultActivityLifecycleCallbacks mCallbacks = new DefaultActivityLifecycleCallbacks() {
        @Override
        public void onActivityResumed(Activity activity) {
            // 是不是监听的当前Activity的生命周期
            if (activity == mActivity) {
//                Log.e(TAG, "activity --> " + activity + "  context-->" + getContext());
                // 开启轮播
                mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
            }
            super.onActivityResumed(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (activity == mActivity) {
                // 停止轮播
                mHandler.removeMessages(SCROLL_MSG);
            }
            super.onActivityPaused(activity);
        }
    };
}
