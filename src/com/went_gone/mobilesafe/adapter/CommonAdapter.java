package com.went_gone.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Went_Gone on 2016/1/25.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> mDatas;
    protected Context context;
    protected LayoutInflater inflater;
    protected int layoutId;

    /*public CommonAdapter(Context context,List<T> mDatas){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }*/
    public CommonAdapter(Context context,List<T> mDatas,int layoutId){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = ViewHolder.get(context,convertView,parent, layoutId,position);
        convert(holder,getItem(position));
        return holder.getConvertView();
    }

    /**
     * 获取内容
     * @param holder  viewHolder
     * @param t  数据
     */
    public abstract void convert(ViewHolder holder,T t);
}
