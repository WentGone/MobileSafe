package com.went_gone.mobilesafe.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class InterceptViewPagerAdapter extends PagerAdapter {
	private List<View> mViews = new ArrayList<View>();

	public InterceptViewPagerAdapter(List<View> views) {
		this.mViews = views;
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mViews.get(position);
		if (view.getParent()!=null) {
			container.removeView(view);
		}
		container.addView(view);
		return view;
	}
	
}
