package com.kevin.library;

import android.view.View;

/**
 * Created by Kevin on 2018/3/16.
 * Adapter模式的实现，数据是抽象的，由调用者返回具体的数据
 */

public abstract class BannerAdapter {
    /**
     * 根据位置获取ViewPager里面的子View
     */
    public abstract View getView(int position,View convertView);
    /**
     * 获取轮播的数量
     */
    public abstract int getCount();

    /**
     * 根据位置获取广告位描述
     */
    public String getBannerDesc(int position){
        return "";
    }
}
