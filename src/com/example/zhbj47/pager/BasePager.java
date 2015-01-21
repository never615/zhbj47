package com.example.zhbj47.pager;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhbj47.MainActivity;
import com.example.zhbj47.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public abstract class BasePager {

	public Context context;
	public View view;
	public TextView txt_title;
	public SlidingMenu slidingMenu;
	public ImageButton imgbtn_text;

	public BasePager(Context context) {
		this.context = context;

		view = initView();
		
		slidingMenu = ((MainActivity)context).getSlidingMenu();
	}

	public abstract View initView();

	public abstract void initData();

	/**
	 * 返回当前页面的view
	 * 
	 * @return
	 */
	public View getRootView() {
		return view;
	}

	/**
	 * 联网请求数据的操作
	 * 
	 * @param method
	 * @param url
	 * @param params
	 * @param callBack
	 */
	public void requestData(HttpMethod method, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(method, url, params, callBack);
	}

	/**
	 * 初始化标题头
	 */
	public void initTitleBar() {
		Button btn_left = (Button) view.findViewById(R.id.btn_left);
		btn_left.setVisibility(View.GONE);

		ImageButton imgbtn_left = (ImageButton) view
				.findViewById(R.id.imgbtn_left);
		imgbtn_left.setImageResource(R.drawable.img_menu);
		// imgbtn_left.setBackgroundResource(resid);
		imgbtn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 缩回弹出的处理方法
				slidingMenu.toggle();
			}
		});

		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.VISIBLE);

		imgbtn_text = (ImageButton) view
				.findViewById(R.id.imgbtn_text);
		imgbtn_text.setVisibility(View.GONE);

		ImageButton imgbtn_right = (ImageButton) view
				.findViewById(R.id.imgbtn_right);
		imgbtn_right.setVisibility(View.GONE);

		ImageButton btn_right = (ImageButton) view.findViewById(R.id.btn_right);
		btn_right.setVisibility(View.GONE);
	}

}
