package com.kevin.recyclerviewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.recyclerviewtest.dragitemanimatoruse.DragItemAnimatorActivity;
import com.kevin.recyclerviewtest.R;

import java.util.List;


/**
 * Created by Kevin on 2018/3/8.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<DragItemAnimatorActivity.ItemBean> mDataList;
    public RecyclerViewAdapter(Context context, List<DragItemAnimatorActivity.ItemBean> dataList){
        this.mContext = context;
        this.mDataList = dataList;
        this.mInflater = LayoutInflater.from(mContext);
    }
    /**
     * 创建条目ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType view的类型可以用来显示多列表布局等等
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_drag_sort_delete, parent, false);
        return new ViewHolder(view);
    }
    /**
     * 绑定ViewHolder设置数据
     *
     * @param holder
     * @param position 当前位置
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DragItemAnimatorActivity.ItemBean data = mDataList.get(position);
        holder.item_text.setText(data.text);
        holder.item_image.setImageResource(data.icon);
    }

    /**
     *
     * @return  总共有多少条数据
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * RecyclerView的Adapter需要一个ViewHolder必须要extends RecyclerView.ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_text;
        ImageView item_image;
        public ViewHolder(View itemView) {
            super(itemView);
            // 在创建的时候利用传递过来的View去findViewById
            item_text = itemView.findViewById(R.id.item_text);
            item_image = itemView.findViewById(R.id.item_image);
        }
    }


}
