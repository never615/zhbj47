package com.example.zhbj47.pager;

import com.example.zhbj47.bean.NewCenter.NewCenterItem;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class TopicPager extends BasePager {

	private NewCenterItem newCenterItem;
	public TopicPager(Context context, NewCenterItem newCenterItem) {
		super(context);
		this.newCenterItem = newCenterItem;
	}

	@Override
	public View initView() {
		TextView textView=new TextView(context);
		textView.setText("话题");
		return textView;
	}

	@Override
	public void initData() {

	}

}
