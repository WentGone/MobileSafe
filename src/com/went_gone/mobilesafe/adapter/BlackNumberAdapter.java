package com.went_gone.mobilesafe.adapter;

import java.util.List;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.dao.BlackNumberDao;
import com.went_gone.mobilesafe.entity.BlackNumberEntity;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

//优化
//1.复用convertView
//2.对findViewById次数的优化，使用ViewHolder
//3.将ViewHolder定义成静态，不会创建多个对象
//4.ListView如果有多个条目，做分页算法，每一次加载(20)条,逆序返回
public class BlackNumberAdapter extends BaseAdapter{
	private List<BlackNumberEntity> mBlackNumbers;
	private Context context;
	private boolean[] isClick;
	private BlackNumberDao mDao;
	private int mCount;
	
	public BlackNumberAdapter(List<BlackNumberEntity> mBlackNumbers,
			Context context,int index) {
		super();
		this.mBlackNumbers = mBlackNumbers;
		this.context = context;
		this.mCount = index;
		isClick = new boolean[mCount];
		mDao = BlackNumberDao.getInstance();
	}

	
	@Override
	public int getCount() {
		return mBlackNumbers.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final BlackNumberEntity blackNumberEntity = mBlackNumbers.get(position);
		final ViewHolder vh;
		if (convertView==null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_blacknumber,null);
			vh = new ViewHolder();
			vh.tv_Name = (TextView) convertView.findViewById(R.id.item_LV_BlackNumber_name);
			vh.tv_Phone = (TextView) convertView.findViewById(R.id.item_LV_BlackNumber_phone);
			vh.tv_Mode = (TextView) convertView.findViewById(R.id.item_LV_BlackNumber_mode);
			vh.linlayout_operate = (LinearLayout) convertView.findViewById(R.id.item_LV_BlackNumber_LinLayout);
			vh.tv_Delete = (TextView) convertView.findViewById(R.id.item_LV_BlackNumber_Delete);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (blackNumberEntity.getName()==null) {
			vh.tv_Name.setText("");
		}else {
			vh.tv_Name.setText(blackNumberEntity.getName());
		}
		if (blackNumberEntity.getMode().equals("1")) {
			vh.tv_Mode.setText("(拦截短信)");
		}
		if (blackNumberEntity.getMode().equals("2")) {
			vh.tv_Mode.setText("(拦截电话)");
		}
		if (blackNumberEntity.getMode().equals("3")) {
			vh.tv_Mode.setText("(拦截短信和电话)");
		}
		vh.tv_Phone.setText(blackNumberEntity.getPhone());
		
		//通过isClick数组来显示该哪条显示出设置选项
		if (isClick[position]) {
			if (vh.linlayout_operate.getVisibility() == View.GONE) {
				vh.linlayout_operate.setVisibility(View.VISIBLE);
			}else {
				vh.linlayout_operate.setVisibility(View.GONE);
			}
		}else {
			vh.linlayout_operate.setVisibility(View.GONE);
		}
		
		//给每个条目设置监听
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i = 0; i < isClick.length; i++) {
					isClick[i] = false;
				}
				isClick[position] = true;
				notifyDataSetChanged();
			}
		});
		
		vh.tv_Delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDao.delete(blackNumberEntity.getPhone());
//				mBlackNumbers = mDao.findAll();
				mBlackNumbers.remove(position);
				if (this != null) {
					isClick = new boolean[mCount--];
					notifyDataSetChanged();
				}
				ToastUtil.show(context, "已从黑名单中删除");
			}
		});
		return convertView;
	}
	class ViewHolder{
		TextView tv_Name;
		TextView tv_Phone;
		TextView tv_Mode;
		LinearLayout linlayout_operate;
		TextView tv_Delete;
	}
}
