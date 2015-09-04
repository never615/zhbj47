package com.example.zhbj47.pager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj47.NewDetailActivity;
import com.example.zhbj47.R;
import com.example.zhbj47.adapter.HMAdapter;
import com.example.zhbj47.bean.NewBean;
import com.example.zhbj47.bean.NewBean.News;
import com.example.zhbj47.utils.GsonUtils;
import com.example.zhbj47.utils.HMApi;
import com.example.zhbj47.utils.SharedPreferencesUtil;
import com.example.zhbj47.view.RefreshListView;
import com.example.zhbj47.view.RefreshListView.OnRefreshListener;
import com.example.zhbj47.view.RollViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 网易新闻中心新闻页页面 TabPageIndicator
 * 
 * @author rong
 * 
 */
public class NewsCenterItemPager extends BasePager {

	/**
	 * 传递数据的url
	 */
	private String url;
	/**
	 * 加载更多数据的时候传递的url
	 */
	private String moreUrl;
	/**
	 * 新闻的bean对象
	 */
	private NewBean newBean;
	/**
	 * sp 中存储点击过的条目的id
	 */
	private String IDS = "ids";
	private String TAG = "NewsCenterItemPager";
	private MyAdapter myAdapter;
	private BitmapUtils bitmapUtils;

	@ViewInject(R.id.dots_ll)
	private LinearLayout dots_ll;

	@ViewInject(R.id.top_news_title)
	private TextView top_news_title;

	/**
	 * 已读条目的集合
	 */
	private List<String> idList = new ArrayList<String>();

	/**
	 * 记录底部listview对应的数据集合
	 */
	private List<News> newList = new ArrayList<NewBean.News>();

	/**
	 * 放置轮播图的位置
	 */
	@ViewInject(R.id.top_news_viewpager)
	private LinearLayout top_news_viewpager;

	/**
	 * 自定义的listview
	 */
	@ViewInject(R.id.lv_item_news)
	private RefreshListView lv_item_news;

	/**
	 * 图片对应链接的集合
	 */
	private List<String> imageUrlList = new ArrayList<String>();
	/**
	 * 文字所在的集合
	 */
	private List<String> titleList = new ArrayList<String>();
	/**
	 * 封装点对应的view对象集合
	 */
	private List<View> viewList = new ArrayList<View>();
	/**
	 * 轮播图
	 */
	private View layout_roll_view;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param url
	 */
	public NewsCenterItemPager(Context context, String url) {
		super(context);
		this.url = url;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public View initView() {

		// 轮播图
		layout_roll_view = View.inflate(context, R.layout.layout_roll_view,
				null);
		ViewUtils.inject(this, layout_roll_view);

		view = View.inflate(context, R.layout.frag_item_news, null);
		ViewUtils.inject(this, view);
		// 添加轮播图要自定义的listview的头上
		lv_item_news.addCustomerView(layout_roll_view);

		lv_item_news.setOnRefreshListener(new OnRefreshListener() {
			// 请求网络的操作,true刷新操作，把原有数据清除，然后获取最新数据
			@Override
			public void pullDownRefresh() {
				getData(url, true);
			}

			// 上拉加载操作，false，在原有数据的基础上，再添加一部分数据
			@Override
			public void loadMore() {
				getData(moreUrl, false);
			}
		});

		// listview 条目的点击事件
		lv_item_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 如果条目没有被点击过的话,进入执行
				if (!newList.get(position - 1).isRead) {

					newList.get(position - 1).isRead = true;

					// 拿到已经存储的id
					String defId = SharedPreferencesUtil.getStringData(context,
							IDS, "");
					// 存储当前点击条目的id 通过#号连接
					SharedPreferencesUtil.saveStringData(context, IDS, defId
							+ "#" + newList.get(position - 1).id);
					// 通知listview刷新
					myAdapter.notifyDataSetChanged();
				}
				
				//点击后的操作，进入应用详情页
				
				//跳转到新的Activity
				Intent intent = new Intent(context,NewDetailActivity.class);
				//展示详情界面的url地址，webView--->Html
				intent.putExtra("url", newList.get(position-1).url);
				context.startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void initData() {
		// 检查本地缓存
		String result = SharedPreferencesUtil.getStringData(context,
				HMApi.BASE_URL + url, "");
		if (!TextUtils.isEmpty(result)) {
			// 解析
			processData(result, true);
		}

		// 第一次进入应用，等同于刷新操作
		getData(url, true);
	}

	/**
	 * 联网获取数据
	 */
	private void getData(final String url, final boolean b) {
		// 在要获取数据之前，拿到已读条目的集合
		String stringId = SharedPreferencesUtil.getStringData(context, IDS, "");
		String[] split = stringId.split("#");
		
		
		idList.clear();
		for (int i = 0; i < split.length; i++) {
			//System.out.println("split[i]:"+split[i]);
			idList.add(split[i]);
		}
		System.out.println("idList:"+idList);

		if (!TextUtils.isEmpty(url)) {
			System.out.println("联网获取数据");
			requestData(HttpMethod.GET, HMApi.BASE_URL + url, null,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							// 联网获取成功，解析，存储到本地
							processData(responseInfo.result, b);
							SharedPreferencesUtil.saveStringData(context,
									HMApi.BASE_URL + url, responseInfo.result);

						}

						@Override
						public void onFailure(HttpException error, String msg) {
						}
					});
		} else {
			Toast.makeText(context, "没有数据......", 0).show();
			// 虽然没有加载数据，仍然有手势的滑动，会显示脚底，隐藏操作
			lv_item_news.finish();
		}

	}

	/**
	 * 解析数据,显示到界面
	 * 
	 * @param result
	 */
	private void processData(String result, boolean b) {
		System.out.println("processData");
		newBean = GsonUtils.json2Bean(result, NewBean.class);
		// Log.i(TAG, newBean.data.news.get(0).title);

		// 获得更多的链接
		moreUrl = newBean.data.more;
		/**
		 * 处理轮播图的操作
		 */
		if (newBean.data.topnews.size() > 0) {

			// 判断是要刷新还是要加载

			// 轮播图的操作
			if (b) {
				imageUrlList.clear();
				titleList.clear();
				for (int i = 0; i < newBean.data.topnews.size(); i++) {
					imageUrlList.add(newBean.data.topnews.get(i).topimage);
					titleList.add(newBean.data.topnews.get(i).title);
				}

				// 把数据显示到界面 图片 标题 滚动点

				// 初始化 第一个点位选中状态
				initDot();

				// 轮播图 自定义
				RollViewPager rollViewPager = new RollViewPager(context,
						viewList);
				// 轮播图文字
				rollViewPager.initTitle(titleList, top_news_title);
				// 轮播图图片
				rollViewPager.initImageUrl(imageUrlList);
				// 轮播图viewPager的适配器
				rollViewPager.startRoll();

				// 将做好的轮播图添加到布局中
				top_news_viewpager.removeAllViews();
				top_news_viewpager.addView(rollViewPager);
			}
		}

		/**
		 * 下方listview的处理
		 */
		if (newBean.data.news.size() > 0) {

			// 判断是要刷新还是要加载更多
			if (b) {
				// 将原有的数据清除掉，然后重新加载
				newList.clear();
			}
			newList.addAll(newBean.data.news);
			
			//根据拿到的已读数据的集合，重新标识，集合
			for(int i=0;i<newList.size();i++){
				System.out.println("newList.get(i).id::"+newList.get(i).id);
				if(idList.contains(newList.get(i).id)){
					System.out.println("运行了没？？？");
					//已读
					newList.get(i).isRead = true;
				}else{
					newList.get(i).isRead = false;
				}
			}

			if (myAdapter == null) {
				myAdapter = new MyAdapter(newList, context);
				lv_item_news.setAdapter(myAdapter);
			} else {
				myAdapter.notifyDataSetChanged();
			}

			// 做完刷新业务之后，调用擦屁股方法。
			lv_item_news.finish();

			// TODO 老师上课报错了，然后把它移到上面了，为什么？
			// 我这么做没有报错
			/*
			 * // 设置轮播图到listview的头上 // listview的头数量超过一个 if
			 * (lv_item_news.getHeaderViewsCount() < 1) {
			 * lv_item_news.addHeaderView(layout_roll_view); }
			 */
		}

	}

	/**
	 * listview的适配器
	 * 
	 * @author rong
	 * 
	 */
	class MyAdapter extends HMAdapter<News> {

		public MyAdapter(List<News> list, Context context) {
			super(list, context);
			// System.out.println("list" + list);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.layout_news_item,
						null);
			}
			ImageView iv_img = (ImageView) convertView
					.findViewById(R.id.iv_img);
			TextView tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			TextView tv_pub_date = (TextView) convertView
					.findViewById(R.id.tv_pub_date);

			bitmapUtils.display(iv_img, list.get(position).listimage);
			tv_title.setText(list.get(position).title);
			tv_pub_date.setText(list.get(position).pubdate);

			// 在显示listview的时候 getView 返回的时候，要标识出已读的条目
			if (list.get(position).isRead) {
				// 高亮显示
				tv_title.setTextColor(Color.RED);
			} else {
				// 默认颜色
				tv_title.setTextColor(Color.BLACK);
			}

			return convertView;
		}

	}

	/**
	 * 初始化点坐标的方法
	 */
	private void initDot() {

		dots_ll.removeAllViews();
		viewList.clear();

		for (int i = 0; i < newBean.data.topnews.size(); i++) {
			View view = new View(context);
			if (i == 0) {
				view.setBackgroundResource(R.drawable.dot_focus);
			} else {
				view.setBackgroundResource(R.drawable.dot_normal);
			}
			// 设置宽高
			LayoutParams layoutParams = new LayoutParams(5, 5);
			// 间距
			layoutParams.setMargins(6, 0, 6, 0);
			// 添加view到界面
			dots_ll.addView(view, layoutParams);
			viewList.add(view);
		}
	}

}
