package com.example.zhbj47.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SettingPager extends BasePager {

	public SettingPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(context);
		textView.setText("设置");
		return textView;
	}

	@Override
	public void initData() {

	}

}
