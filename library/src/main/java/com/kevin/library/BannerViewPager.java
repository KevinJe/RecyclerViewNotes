package com.kevin.library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2018/3/16.
 * 轮播图的实现
 */

public class BannerViewPager extends ViewPager {
    private static final String TAG = "BannerViewPager";
    private BannerAdapter mAdapter;
    //定时执行的handler
    private Handler mHandler;
    // 实现自动轮播 - 发送消息的msgWhat
    private final int SCROLL_MSG = 0x0011;
    // 实现自动轮播 - 页面切换间隔时间
    private int mCutDownTime = 3500;
    // 改变ViewPager切换的速率 - 自定义的页面切换的Scroller
    private BannerScroller mScroller;
    //重新开始轮播的标志位
    private boolean reStart = true;
    //界面复用的View
    private List<View> mConvertViews;
    //当前的Activity
    private Activity mActivity;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        // 改变ViewPager切换的速率
        // duration 持续的时间  局部变量
        // 改变 mScroller( private ) 通过反射设置
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            mScroller = new BannerScroller(context);
            // 设置为强制改变private
            field.setAccessible(true);
            // 设置参数  第一个object当前属性在哪个类  第二个参数代表要设置的值
            field.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                //循环执行
                startScroll();
            }
        };
    }

    /**
     * 实现自动轮播
     */
    public void startScroll() {
        // 清除消息
        mHandler.removeMessages(SCROLL_MSG);
        // 消息  延迟时间  让用户自定义  有一个默认  3500
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
        Log.e(TAG, "startScroll: 自动轮播");
    }

    /**
     * 销毁Handler停止发送消息，解决内存泄漏
     * 但是此方法只要相应的View不可见就会调用，在这里如果ViewPager不可见了，包括向下滑动列表时ViewPager
     * 看不见了，也会执行此方法。所以，就会造成当你再次向上滑动时，ViewPager可见了，但是不会自动轮播了
     * 原因是这里将Handler赋值为空了
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            // 销毁Handler的生命周期
            mHandler.removeMessages(SCROLL_MSG);
            mHandler = null;
            // 解除注册
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
            reStart = false;
            Log.e(TAG, "onDetachedFromWindow: handler被销毁");
        }
        super.onDetachedFromWindow();
    }

    /**
     * 为了解决上边的问题需要在这里处理ViewPager可见时重新开启Handler的逻辑
     */
    @Override
    protected void onAttachedToWindow() {
        initHandler();
        if (!reStart) {
            mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
        }
        //注册生命周期
        mActivity.getApplication().registerActivityLifecycleCallbacks(mLifecycleCallbacks);
        reStart = true;
        Log.e(TAG, "onAttachedToWindow: handler开启");
        super.onAttachedToWindow();
    }

    /**
     * 设置切换页面动画持续的时间
     */
    public void setScrollerDuration(int scrollerDuration) {
        mScroller.setScrollerDuration(scrollerDuration);
    }

    /**
     * 设置自定义的BannerAdapter,这里很巧妙的利用了Adapter模式，将抽象逻辑放到了BannerAdapter
     * 但是实际设置的Adapter为BannerPagerAdapter
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        // 设置父类 ViewPager的adapter
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * ViewPager的Adapter
     */
    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //返回一个int型的最大值，为了无限轮播
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 官方推荐这么写  源码
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //由于这里getCount返回了Integer.MAX_VALUE，所以智能利用上面的mAdapter
            //返回的getCount来求余，得到真实的位置
            View view = mAdapter.getView(position % mAdapter.getCount(), getConvertView());
            Log.e(TAG, "positon " + position + "  count " + mAdapter.getCount());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //移除页面
            container.removeView((View) object);
            //销毁时将界面保存，复用
            mConvertViews.add((View) object);
            Log.e(TAG, "destroyItem: " + position);
        }
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

    private Application.ActivityLifecycleCallbacks mLifecycleCallbacks = new DefaultActivityLifecycleCallbacks() {
        @Override
        public void onActivityResumed(Activity activity) {
            // 是不是监听的当前Activity的生命周期
            if (activity == mActivity) {
                Log.e(TAG, "activity --> " + activity + "  context-->" + getContext());
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
