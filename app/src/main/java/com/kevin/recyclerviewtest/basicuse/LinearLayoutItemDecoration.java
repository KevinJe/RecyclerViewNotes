package com.kevin.recyclerviewtest.basicuse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kevin on 2018/3/11.
 */

public class LinearLayoutItemDecoration extends RecyclerView.ItemDecoration {
    //分割线的draable
    private Drawable mDivider;
    public LinearLayoutItemDecoration(Context context,int drawableResId) {
        mDivider = ContextCompat.getDrawable(context,drawableResId);
    }
    /**
     * item之间的偏移量，可以用此方法来决定item之间的间隔
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        // 第一个item不需要画线，所以，不需要预留位置
        if (position != 0) {
            // 每个item预留顶部的空间来画线
            outRect.top = mDivider.getIntrinsicHeight();
        }
    }

    /**
     * 在这里可以画分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            int left = parent.getPaddingLeft();
//            int right = parent.getWidth() - parent.getPaddingRight();
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();
        //得到item的数量
        int childCount = parent.getChildCount();
        //第一个item不需要画线
        for (int i = 1; i < childCount; i++) {
            View child = parent.getChildAt(i);
            //得到item的布局参数
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //分割线的底部就是item的上边 - margin
            rect.bottom = child.getTop() - params.topMargin;
            // 分割线上部就是分割线底部减去分割线的高度
            rect.top = rect.bottom - mDivider.getIntrinsicHeight();
            //画矩形的分割线
            mDivider.setBounds(rect);
            mDivider.draw(c);
        }
    }

}
