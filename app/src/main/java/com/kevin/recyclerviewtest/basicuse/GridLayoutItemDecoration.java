package com.kevin.recyclerviewtest.basicuse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kevin on 2018/3/11.
 */

public class GridLayoutItemDecoration extends RecyclerView.ItemDecoration {
    //分割线的draable
    private Drawable mDivider;

    public GridLayoutItemDecoration(Context context, int drawableResId) {
        mDivider = ContextCompat.getDrawable(context, drawableResId);
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
        //GridView的情况下需要预留出右侧与下侧的空间
        int right = mDivider.getIntrinsicHeight();
        int bottom = mDivider.getIntrinsicWidth();
        //获取当前的位置
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (isLastColumn(position, parent)) {
            right = 0;
        }
        if (isLastRow(position, parent)) {
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    /**
     * 判断是否是最后一行
     *
     * @param itemPosition
     * @param parent
     * @return
     */
    private boolean isLastRow(int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int itemCount = parent.getAdapter().getItemCount();
        // item数量和span数量求余，为零代表行数正好为item数除span数，否则就又多了一行
        int rowNumber = itemCount % spanCount == 0? itemCount/spanCount:(itemCount/spanCount) + 1;
        if (itemPosition > ((rowNumber - 1)*spanCount - 1)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是最后一列
     *
     * @param itemPosition
     * @param parent
     * @return
     */
    private boolean isLastColumn(int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        if ((itemPosition + 1) % spanCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取Span数量
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            int spanCount = gridLayoutManager.getSpanCount();
            return spanCount;
        }
        return 1;
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
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    /**
     * 绘制水平方向上的分割线
     *
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 绘制竖直分割线
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    /**
     * 竖直方向上的分割线
     * @param c
     * @param parent
     */
    /*private void drawVertical(Canvas c, RecyclerView parent) {
        Rect rect = new Rect();
        rect.top = parent.getPaddingTop();
        rect.bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0;i < childCount;i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            rect.left = child.getRight() + params.leftMargin;
            rect.right = rect.left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(rect);
            mDivider.draw(c);
        }
    }*/

    /**
     * 水平方向上的分割线
     * @param c
     * @param parent
     */
    /*private void drawHorizontal(Canvas c, RecyclerView parent) {
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
    }*/

}
