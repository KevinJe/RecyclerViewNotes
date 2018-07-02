package com.kevin.recyclerviewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kevin on 2018/3/14.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    // 用来存放子View减少findViewById的次数
    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 通过Id寻找View
     *
     * @param viewId View的Id
     * @param <T>    不知道View的类型，所以使用泛型
     * @return
     */
    public <T extends View> T getView(int viewId) {
        // 先从缓存中找
        View view = mViews.get(viewId);
        if (view == null) {
            //缓存中没有，再去findViewById
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置TextView的文本
     *
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
        //返回自己，实现链式的调用
        return this;
    }

    /**
     * 设置View的可见性
     *
     * @param viewId     view的Id
     * @param visibility 是否可见
     * @return
     */
    public ViewHolder setViewVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }

    /**
     * 设置ImageView的资源
     *
     * @param viewId
     * @param resourceId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resourceId) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageResource(resourceId);
        }
        return this;
    }

    /**
     * 设置条目点击事件
     *
     * @param listener 点击事件监听器
     */
    public void setOnItemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    /**
     * 设置条目长按事件
     *
     * @param listener 长按事件监听器
     */
    public void setOnItemLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
    }

    /**
     * 设置图片通过路径,这里稍微处理得复杂一些，因为考虑加载图片的第三方可能不太一样
     * 也可以直接写死
     */
      public ViewHolder setImageByUrl(int viewId,HolderImageLoader imageLoader){
          ImageView view = getView(viewId);
          if (imageLoader == null) {
              throw new NullPointerException("ImageLoader is null");
          }
          imageLoader.displayImage(view.getContext(),view,imageLoader.getImagePath());
          return this;
      }

    /**
     * 图片加载，这里稍微处理得复杂一些，因为考虑加载图片的第三方可能不太一样
     * 也可以不写这个类
     */
    public abstract static class HolderImageLoader{
        private String mImagePath;
        public HolderImageLoader(String imagePath){
            this.mImagePath = imagePath;
        }
        public String getImagePath(){
            return mImagePath;
        }
        //加载图片时必须复写这个方法
        public abstract void displayImage(Context context,ImageView imageView,String imagePath);
    }
}
