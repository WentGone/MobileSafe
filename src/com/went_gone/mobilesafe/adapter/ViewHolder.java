package com.went_gone.mobilesafe.adapter;

import android.R.integer;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Went_Gone on 2016/1/25.
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public ViewHolder(Context context,ViewGroup parent,int layoutId,int position){
        this.mPosition = position;
        this.mViews = new SparseArray<View>();

        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
        if (convertView == null){
            return new ViewHolder(context,parent,layoutId,position);
        }else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;//更新position
            return holder;
        }
    }

    /**
     * 通过viewId获取控件
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if (view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置TextView的内容
     * @param viewId
     * @param content
     * @return
     */
    public ViewHolder setTextViewText(int viewId,String content){
        TextView tv = getView(viewId);
        tv.setText(content);
        return this;
    }
    
    public ViewHolder setImageResource (int viewId,int imageId) {
    	ImageView iv = getView(viewId);
    	iv.setImageResource(imageId);
		return this;
	}

}
