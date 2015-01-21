package com.example.zhbj47.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(context);
		textView.setText("智慧服务");
		return textView;
	}

	@Override
	public void initData() {

	}

}
