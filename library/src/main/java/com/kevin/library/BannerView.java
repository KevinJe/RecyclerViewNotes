package com.kevin.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Kevin on 2018/3/17.
 * 自定义轮播图
 */

public class BannerView extends RelativeLayout {
    private static final String TAG = "BannerView";
    //自定义的ViewPager
    private BannerViewPager mBannerViewPager;
    //自定义的Adapter
    private BannerAdapter mBannerAdapter;
    //banner底部的图片描述
    private TextView mBannerDes;
    //banner底部指示点的容器
    private LinearLayout mDotContainer;
    //banner底部的容器
    private RelativeLayout mBottomView;
    //banner底部容器的颜色，默认透明
    private int mBottomViewColor = Color.TRANSPARENT;

    private Context mContext;
    //指示点的选中值
    private Drawable mDotIndicatorFocusDrawable;
    //指示点的平常值
    private Drawable mDotIndicatorNormalDrawable;
    //指示点的大小
    private int mDotSize = 8;
    //指示点的距离
    private int mDotDistance = 8;
    //指示点的位置，默认右面
    private int mDotGravity = 1;
    //宽度比
    private float mWidthProportion = 0;
    //高度比
    private float mHeightProportion = 0;

    // 当前位置
    private int mCurrentPosition = 0;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //加载布局
        inflate(context, R.layout.layout_banner_view, this);
        initAttribute(attrs);
        initViews();
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttribute(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mDotIndicatorFocusDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if (mDotIndicatorFocusDrawable == null) {
            //如果布局中没有写颜色，给一个默认值
            mDotIndicatorFocusDrawable = new ColorDrawable(Color.RED);
        }
        mDotIndicatorNormalDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if (mDotIndicatorNormalDrawable == null) {
            //如果布局中没有写颜色，给一个默认值
            mDotIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        }
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotSize, dip2px(mDotSize));
        mDotDistance = (int) array.getDimension(R.styleable.BannerView_dotDistance, dip2px(mDotDistance));
        mDotGravity = array.getInt(R.styleable.BannerView_dotGravity, mDotGravity);
        mBottomViewColor = array.getColor(R.styleable.BannerView_bottomViewColor, mBottomViewColor);
        mWidthProportion = array.getFloat(R.styleable.BannerView_widthProportion, mWidthProportion);
        mHeightProportion = array.getFloat(R.styleable.BannerView_widthProportion, mHeightProportion);
        //使用结束一定要回收
        array.recycle();
    }

    /**
     * 初始化View
     */
    private void initViews() {
        mBannerViewPager = findViewById(R.id.banner_view_pager);
        mBannerDes = findViewById(R.id.banner_desc);
        mDotContainer = findViewById(R.id.dot_container);
        mBottomView = findViewById(R.id.banner_bottom_view);
        mBottomView.setBackgroundColor(mBottomViewColor);
    }

    /**
     * 为ViewPager设置Adapter，其实是为取值，ViewPager真实的Adapter在自定义的ViewPager中
     *
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter) {
        mBannerAdapter = adapter;
        mBannerViewPager.setAdapter(adapter);
        initDotIndicator();
        mBannerViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 监听当前选中的位置
                pageSelect(position);
            }
        });
        String desc = mBannerAdapter.getBannerDesc(0);
        mBannerDes.setText(desc);
        //动态指定宽高
        if (mHeightProportion == 0 || mWidthProportion == 0) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                // 动态指定宽高  计算高度
                int width = getMeasuredWidth();
                // 计算高度
                int height = (int) (width * mHeightProportion / mWidthProportion);
                // 指定宽高
                getLayoutParams().height = height;
                mBannerViewPager.getLayoutParams().height = height;
            }
        });
    }

    /**
     * 页面切换的回调
     *
     * @param position
     */
    private void pageSelect(int position) {
        //先得到第一条的指示点
        DotIndicatorView oldIndicatorView = (DotIndicatorView)
                mDotContainer.getChildAt(mCurrentPosition);
        //设置为平常颜色
        oldIndicatorView.setDrawable(mDotIndicatorNormalDrawable);
        //得到当前的真实位置
        mCurrentPosition = position % mBannerAdapter.getCount();
        //将真实位置的现在的page处的指示点变为选中颜色
        DotIndicatorView newIndicatorViewmDotContainer = (DotIndicatorView)
                mDotContainer.getChildAt(mCurrentPosition);
        newIndicatorViewmDotContainer.setDrawable(mDotIndicatorFocusDrawable);
        //得到现在位置的描述
        String desc = mBannerAdapter.getBannerDesc(mCurrentPosition);
        mBannerDes.setText(desc);
    }

    /**
     * 初始化指示点
     */
    private void initDotIndicator() {
        //获取广告数量
        int count = mBannerAdapter.getCount();
        //根据指示点的位置设置位置
        mDotContainer.setGravity(getDotGravity());
        for (int i = 0; i < count; i++) {
            DotIndicatorView indicatorView = new DotIndicatorView(mContext);
            //设置宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            //设置间距
            params.leftMargin = mDotDistance;
            indicatorView.setLayoutParams(params);
            if (i == 0) {
                indicatorView.setDrawable(mDotIndicatorFocusDrawable);
            } else {
                indicatorView.setDrawable(mDotIndicatorNormalDrawable);
            }
            mDotContainer.addView(indicatorView);
        }
    }

    /**
     * 开始自动轮播
     */
    public void startScroll() {
        mBannerViewPager.startScroll();
    }

    /**
     * 把dip转成px
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

    public int getDotGravity() {
        switch (mDotGravity) {
            case 0:
                return Gravity.CENTER;
            case -1:
                return Gravity.LEFT;
            case 1:
                return Gravity.RIGHT;
        }
        return Gravity.LEFT;
    }
}
