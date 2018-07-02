package com.kevin.recyclerviewtest.widget.banner;

import android.view.View;

/**
 * Created by Kevin Jern on 2018/7/1 19:54.
 */
public abstract class BanAdapter {
    /**
     * 获取ViewPager的子View
     *
     * @param position
     * @param convertView
     * @return
     */
    public abstract View getView(int position, View convertView);

    /**
     * 获得子View的数量
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 获得指定位置处的banner描述
     *
     * @param position
     * @return
     */
    public  String getBannerDes(int position){
        return "";
    }

}
