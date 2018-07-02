package com.kevin.recyclerviewtest.commonadpteruse;

import android.text.Html;
import android.text.TextUtils;

import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.BaseAdapter;
import com.kevin.recyclerviewtest.adapter.MyViewHolder;
import com.kevin.recyclerviewtest.bean.ArticleBean;

/**
 * Created by Kevin Jern on 2018/6/29 21:59.
 */
public class ArticleAdapter extends BaseAdapter<ArticleBean> {
    private static final String TAG = "ArticleAdapter";

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_home_article;
    }

    @Override
    protected void bindData(MyViewHolder holder, ArticleBean bean, int position) {
        String authorText = "作者： " + "<font color='#0091ea'>" +
                bean.getAuthor() + "</font>";
        String titleText = bean.getTitle();
        // superChapterName可能为空字符，所以进行处理
        String superChapterName = bean.getSuperChapterName();
        if (TextUtils.isEmpty(superChapterName)) {
            superChapterName = "";
        } else {
            superChapterName += " / ";
        }
        String typeText = "分类： " + "<font color='#0091ea'>" +
                superChapterName +
                bean.getChapterName() + "</font>";
        holder.setText(R.id.tv_author, Html.fromHtml(authorText))
                .setText(R.id.tv_time, bean.getNiceDate())
                .setText(R.id.tv_title, Html.fromHtml(titleText))
                .setText(R.id.tv_type, Html.fromHtml(typeText));
    }
}
