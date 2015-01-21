package com.example.zhbj47;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewDetailActivity extends Activity {
	private WebView webView;
	private FrameLayout loading_view;
	private TextView txt_title;
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//数据接收,url就是当前界面要去加载的html界面
		String url = getIntent().getStringExtra("url");
		setContentView(R.layout.act_news_detail);
		
		webView = (WebView) findViewById(R.id.news_detail_wv);
		loading_view = (FrameLayout) findViewById(R.id.loading_view);
		//处理头
		initTitleBar();
		//webView加载一个Html页面的操作
		webView.loadUrl(url);
		//加载完毕后的操作
		webView.setWebViewClient(new WebViewClient(){
			//加载完毕后回去调用的方法
			@Override
			public void onPageFinished(WebView view, String url) {
				loading_view.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});
		//获取修改字体的对象
		settings = webView.getSettings();
	}
	public void initTitleBar(){
		Button btn_left = (Button)findViewById(R.id.btn_left);
		btn_left.setVisibility(View.GONE);
		
		ImageButton imgbtn_left = (ImageButton) findViewById(R.id.imgbtn_left);
		imgbtn_left.setImageResource(R.drawable.back);
		imgbtn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//缩回弹出的处理方法
				finish();
			}
		});
		
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		
		ImageButton imgbtn_text = (ImageButton) findViewById(R.id.imgbtn_text);
		imgbtn_text.setImageResource(R.drawable.icon_textsize);
		imgbtn_text.setVisibility(View.VISIBLE);
		imgbtn_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settings.setTextSize(TextSize.LARGEST);
			}
		});
		
		ImageButton imgbtn_right = (ImageButton)findViewById(R.id.imgbtn_right);
		imgbtn_right.setImageResource(R.drawable.icon_share);
		imgbtn_right.setVisibility(View.VISIBLE);
		
		ImageButton btn_right = (ImageButton) findViewById(R.id.btn_right);
		btn_right.setVisibility(View.GONE);
	}
}
