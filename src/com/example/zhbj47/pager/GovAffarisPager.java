package com.example.zhbj47.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class GovAffarisPager extends BasePager {

	public GovAffarisPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(context);
		textView.setText("政务信息");
		return textView;
	}

	@Override
	public void initData() {

	}

}
