package com.example.zhbj47.view;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhbj47.R;
import com.lidroid.xutils.BitmapUtils;

public class RollViewPager extends ViewPager {
	private BitmapUtils bitmapUtils;
	private List<View> viewList;
	private List<String> imageUrlList;
	private RunnableTask runnableTask;
	private int currentPosition;
	private MyAdapter myAdapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			RollViewPager.this.setCurrentItem(currentPosition);
			startRoll();
		};
	};
	private TextView top_news_title;
	private List<String> titleList;
	private int startX;
	private int startY;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param viewList
	 *            滚动点的集合
	 */
	public RollViewPager(Context context, final List<View> viewList) {
		super(context);
		this.viewList = viewList;
		bitmapUtils = new BitmapUtils(context);
		runnableTask = new RunnableTask();
		this.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// 设置选中图片的配套文字
				top_news_title.setText(titleList.get(arg0));
				// 设置选中图片配套的点
				for (int i = 0; i < viewList.size(); i++) {
					if (i == arg0) {
						viewList.get(arg0).setBackgroundResource(
								R.drawable.dot_focus);
					} else {
						viewList.get(i).setBackgroundResource(
								R.drawable.dot_normal);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 设置轮播图的标题文字
	 * 
	 * @param titleList
	 * @param top_news_title
	 */
	public void initTitle(List<String> titleList, TextView top_news_title) {
		if (titleList.size() > 0 && top_news_title != null) {
			top_news_title.setText(titleList.get(0));
		}
		this.top_news_title = top_news_title;
		this.titleList = titleList;
	}

	/**
	 * 接受轮播图的图片
	 * 
	 * @param imageUrlList
	 */
	public void initImageUrl(List<String> imageUrlList) {
		this.imageUrlList = imageUrlList;
	}

	/**
	 * 设置轮播图的适配器
	 */
	public void startRoll() {
		if (myAdapter == null) {
			myAdapter = new MyAdapter();
			this.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}
		// viewpager按照一定的规则滚动，3秒滚动一次，(定时器,3秒轮训发送消息)

		handler.postDelayed(runnableTask, 3000);
	}

	/**
	 * 循环图片
	 */
	class RunnableTask implements Runnable {

		@Override
		public void run() {
			// 循环播放图片
			currentPosition = (currentPosition + 1) % imageUrlList.size();

			// 发送一个消息，给handler方法处理
			handler.obtainMessage().sendToTarget();
		}

	}

	/**
	 * pager的适配器
	 */
	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageUrlList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(getContext(), R.layout.viewpager_item,
					null);
			ImageView image = (ImageView) view.findViewById(R.id.image);

			// 触摸viewpager则停止滚动 移除handler维护的消息，停止触摸之后，再次startRoll，开始滚动

			// 所有的事件都是先有viewpager捕获，然后给view响应
			// 1，ACTION_DOWN事件由viewpager传递给view，然后view做响应
			// 2,如果按下不滑动抬起，当前事件，就是按下抬起两个动作，action_up也是优先传递给viewpager然后传递给view，view做相应
			// 3,按下滑动，3.1滑动事件同样优先传递给viewpager然后传递给view响应，当滑动到一定距离的时候，view不再响应滑动事件，转而响应
			// action_cancel,view就再也不响应事件，viewpager响应后续的滑动，抬起事件
			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						System.out.println("ACTION_DOWN");
						handler.removeCallbacksAndMessages(null);
						break;
					case MotionEvent.ACTION_UP:
						System.out.println("ACTION_UP");
						startRoll();
						break;
					case MotionEvent.ACTION_CANCEL:
						startRoll();
						break;

					}
					return true;
				}
			});

			// 下载图片逻辑，开源Xutil，
			// 1，下载过后图片需要放置的地点,下载图片的url地址
			// 3级缓存，
			// 第一次打开应用，从网络获取图片，1，文件缓存 （图片url地址） 2，内存缓存（map<url,Bitmap>）
			// 内存缓存的时候，需要考虑内存溢出，LRU算法(最近最少使用算法，最近最少使用的图片放在最前面)，虚拟机运行的大小

			// SoftRefrence 软引用 SmartImageView

			bitmapUtils.display(image, imageUrlList.get(position));
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * 当当前view移除屏幕的时候执行的方法
	 */
	@Override
	protected void onDetachedFromWindow() {
		// 移除handler维护的消息
		// TODO 为什么要移除，就好了，不是创建了12个子条目，
		// 每一个单独维护自己的viewPager吗，上课讲移出界面了，还在维护，移出界面了还维护干嘛
		// 可是既然还在维护，为什么会错位
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}

	// TODO 为什么 写dispatchTouchEvent 不用onTouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(arg0);
	}

	/**
	 * 拦截触摸事件方法
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			int dX = Math.abs(endX - startX);
			int dY = Math.abs(endY - startY);

			if (dY > dX) {
				// 竖直方向移动的多
				// 刷新操作，父控件完成，需要去拦截事件
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				// 水平方向移动的多
				// 判断移动方向
				if ((endX - startX) > 0 && getCurrentItem() == 0) {
					// 模块向前翻页 由父控件处理
					getParent().requestDisallowInterceptTouchEvent(false);

				} else if (getCurrentItem() > 0 && (endX - startX) > 0) {
					// 父控件不去拦截事件 自己处理
					getParent().requestDisallowInterceptTouchEvent(true);
				} else if ((endX - startX) < 0
						&& getCurrentItem() == myAdapter.getCount() - 1) {
					// 处于滚动图的最后一页，由父控件去响应，不拦截
					getParent().requestDisallowInterceptTouchEvent(false);
				} else if ((endX - startX) < 0
						&& getCurrentItem() < myAdapter.getCount()) {
					// 处于中间，拦截，自己处理
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			break;

		}
		return super.dispatchTouchEvent(ev);
	}

}
