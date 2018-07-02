package com.kevin.recyclerviewtest.commonadpteruse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kevin on 2018/3/14.
 */

public class CategoryItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public CategoryItemDecoration(Context context, int drawableResId) {
        mDivider = ContextCompat.getDrawable(context, drawableResId);
    }

    /**
     * 在每个item下预留divider的高度
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mDivider.getIntrinsicHeight();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        //循环遍历
        for (int i = 0; i < childCount; i++) {
            //得到每个item
            View child = parent.getChildAt(i);
            //需要考虑margin的影响
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.topMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
