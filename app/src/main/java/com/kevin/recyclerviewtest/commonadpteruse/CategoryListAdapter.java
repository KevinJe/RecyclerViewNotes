package com.kevin.recyclerviewtest.commonadpteruse;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.kevin.recyclerviewtest.R;
import com.kevin.recyclerviewtest.adapter.CommonRecyclerAdapter;
import com.kevin.recyclerviewtest.adapter.ViewHolder;

import java.util.List;

/**
 * Created by Kevin on 2018/3/12.
 */

public class CategoryListAdapter extends CommonRecyclerAdapter<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> {

    public CategoryListAdapter(Context context, List<ChannelListResult.DataBean.
            CategoriesBean.CategoryListBean> data) {
        super(context, data, R.layout.channel_list_item);
    }

    @Override
    public void convert(ViewHolder holder, final ChannelListResult.DataBean.CategoriesBean.CategoryListBean item) {
        //第一次实现

       /* ImageView channelIcon = holder.itemView.findViewById(R.id.channel_icon);
        Glide.with(mContext).load(item.getIcon_url()).into(channelIcon);
        TextView channelText = holder.itemView.findViewById(R.id.channel_text);
        channelText.setText(item.getName());
        TextView channelTopic = holder.itemView.findViewById(R.id.channel_topic);
        channelTopic.setText(item.getIntro());
        String str = item.getSubscribe_count() + " 订阅 | " +
                "总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
        TextView channelUpdateInfo = holder.itemView.findViewById(R.id.channel_update_info);
        channelUpdateInfo.setText(Html.fromHtml(str));
        ImageView recommendLabel = holder.itemView.findViewById(R.id.recommend_label);
        if (item.isIs_recommend()) {
            recommendLabel.setVisibility(View.VISIBLE);
        } else {
            recommendLabel.setVisibility(View.GONE);
        }*/

        //上面实现的优化，主要减少findViewById的写法
        /*ImageView channelIcon = holder.getView(R.id.channel_icon);
        Glide.with(mContext).load(item.getIcon_url()).into(channelIcon);
        TextView channelText = holder.getView(R.id.channel_text);
        channelText.setText(item.getName());
        TextView channelTopic = holder.getView(R.id.channel_topic);
        channelTopic.setText(item.getIntro());
        String str = item.getSubscribe_count() + " 订阅 | " +
                "总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
        TextView channelUpdateInfo = holder.getView(R.id.channel_update_info);
        channelUpdateInfo.setText(Html.fromHtml(str));
        ImageView recommendLabel = holder.getView(R.id.recommend_label);
        if (item.isIs_recommend()) {
            recommendLabel.setVisibility(View.VISIBLE);
        } else {
            recommendLabel.setVisibility(View.GONE);
        }*/

        //再次进行优化，可以链式调用设置属性
        //加载图片
        holder.setImageByUrl(R.id.channel_icon, new GlideImageLoader(item.getIcon_url()));
        String str = item.getSubscribe_count() + " 订阅 | " +
                "总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
        //链式设置文本
        holder.setText(R.id.channel_text, item.getName())
                .setText(R.id.channel_topic, item.getIntro())
                .setText(R.id.channel_update_info, Html.fromHtml(str));
        //是否是推荐位
        if (item.isIs_recommend()) {
            holder.setViewVisibility(R.id.recommend_label, View.VISIBLE);
        } else {
            holder.setViewVisibility(R.id.recommend_label, View.GONE);
        }
      /*  //点击事件
        holder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        //长按事件
        holder.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "长按了 " + item.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
    }
}


/*public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private Context mContext;
    private List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> mData;
    private LayoutInflater mInflater;

    public CategoryListAdapter(Context context, List<ChannelListResult.DataBean.CategoriesBean.CategoryListBean> datas) {
        this.mContext = context;
        this.mData = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.channel_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ChannelListResult.DataBean.CategoriesBean.CategoryListBean item = mData.get(position);
        Glide.with(mContext).load(item.getIcon_url()).into(holder.channelIcon);
        holder.channelText.setText(item.getName());
        holder.channelTopic.setText(item.getIntro());
        String str = item.getSubscribe_count() + " 订阅 | " +
                "总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
        holder.channelUpdateInfo.setText(Html.fromHtml(str));
        if (item.isIs_recommend()) {
            holder.recommendLabel.setVisibility(View.VISIBLE);
        } else {
            holder.recommendLabel.setVisibility(View.GONE);
        }
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //回调点击事件，接口实现
                    mListener.onItemClick(position);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(position);
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView channelIcon;
        TextView channelText;
        TextView channelTopic;
        TextView channelUpdateInfo;
        ImageView recommendLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            channelIcon = itemView.findViewById(R.id.channel_icon);
            channelText = itemView.findViewById(R.id.channel_text);
            channelTopic = itemView.findViewById(R.id.channel_topic);
            channelUpdateInfo = itemView.findViewById(R.id.channel_update_info);
            recommendLabel = itemView.findViewById(R.id.recommend_label);
        }
    }

    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;


 /*
public void setOnItemClickListener(ItemClickListener listener) {
        this.mListener = listener;

        }

        *//**
 * 点击的接口
 * <p>
 * 长按事件
 * <p>
 * 长按事件
 * <p>
 * 长按事件
 *//*
public interface ItemClickListener {
    void onItemClick(int position);
}

    *//**
 * 长按事件
 *//*
public interface ItemLongClickListener {
    void onItemLongClick(int position);
}

    public void setOnItemLongClickListener(ItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }
}*/
