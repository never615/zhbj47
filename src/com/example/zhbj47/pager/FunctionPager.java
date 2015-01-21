package com.example.zhbj47.pager;

import java.net.URL;

import com.example.zhbj47.utils.HMApi;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class FunctionPager extends BasePager {

	public FunctionPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(context);
		textView.setText("首页");
		return textView;
	}

	/**
	 * 联网请求填充数据
	 */
	@Override
	public void initData() {


	}

}
