package com.kevin.recyclerviewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kevin Jern on 2018/6/29 21:16.
 * ViewHolder
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    // 缓存View
    private SparseArray<View> mViews;

    public MyViewHolder(View itemView) {
        super(itemView);
        if (mViews == null) {
            mViews = new SparseArray<>();
        }
    }

    /**
     * 通过layoutId生成ViewHolder
     *
     * @param parent
     * @param layoutId
     * @return
     */
    public static MyViewHolder createViewHolder(ViewGroup parent, int layoutId) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 通过View生成ViewHolder
     *
     * @param view
     * @return
     */
    public static MyViewHolder createViewHolder(View view) {
        return new MyViewHolder(view);
    }

    /**
     * 通过Id查找View
     *
     * @param viewId id
     * @param <T>    view
     * @return view
     */
    public <T extends View> T getView(int viewId) {
        // 从缓存中查找
        View view = mViews.get(viewId);
        if (view == null) {
            //缓存中没有，再去findViewById
            view = itemView.findViewById(viewId);
            // 将其加入到缓存
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * TextView设置文本
     *
     * @param viewId id
     * @param text   text for TextView
     * @return
     */
    public MyViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
        // 返回自己，实现链式的调用
        return this;
    }

    /**
     * 设置View的可见性
     *
     * @param viewId
     * @param visibility
     * @return
     */
    public MyViewHolder setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }

    /**
     * ImageView设置图片资源
     *
     * @param viewId id
     * @param resId  resId
     * @return
     */
    public MyViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
        return this;
    }

    /**
     * 通过Url设置图片资源
     *
     * @param viewId id
     * @param loader ImageLoader
     * @return
     */
    public MyViewHolder setImageByUrl(int viewId, ImageLoader loader) {
        ImageView imageView = getView(viewId);
        if (loader == null) {
            throw new NullPointerException("ImageLoader must not null");
        }
        if (imageView != null) {
            loader.displayImage(imageView.getContext(), imageView, loader);
        }
        return this;
    }


    public abstract static class ImageLoader {
        private String imagePath;

        public ImageLoader(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }

        /**
         * 想要展示图片直接复写这个方法
         *
         * @param context
         * @param imageView
         * @param loader
         */
        public abstract void displayImage(Context context, ImageView imageView, ImageLoader loader);
    }
}
