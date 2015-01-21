package com.example.zhbj47.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhbj47.R;

public class RefreshListView extends ListView {

	private LinearLayout refresh_header_root;
	private LinearLayout refresh_header_view;
	private ProgressBar refresh_header_progressbar;
	private ImageView refresh_header_imageview;
	private TextView refresh_header_text;
	private TextView refresh_header_time;
	private int refresh_header_height;
	private View constomerView;
	private RotateAnimation rotateAnimationUp;
	private RotateAnimation rotateAnimationDown;
	private int startY = -1;

	private int currentState = PULL_REFRESH;
	private OnRefreshListener onRefreshListener;

	// 下拉刷新
	private static final int PULL_REFRESH = 0;
	// 释放刷新
	private static final int RELEASE_REFRESH = 1;
	// 正在刷新
	private static final int IS_REFRESHING = 2;

	// 默认为没有加载更多
	private boolean isload = false;

	/**
	 * handler
	 */
/*	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 刷新完的善后，归位
			finish();
		};
	};*/
	private LinearLayout refresh_footer_root;
	private int footerHeight;

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 添加刷新头
		initHeader();
		initFoot();

		/**
		 * 滚动监听
		 */
		setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// OnScrollListener.SCROLL_STATE_FLING 飞速滚动
				// OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 慢慢的滚动
				// OnScrollListener.SCROLL_STATE_IDLE 滚动状态改变
				if (scrollState == OnScrollListener.SCROLL_STATE_FLING
						|| scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					if ((getLastVisiblePosition() == getCount() - 1) && !isload) {
						// 加载逻辑
						// 底部的脚，显示出来
						refresh_footer_root.setPadding(0, 0, 0, 0);
						isload = true;
						if (onRefreshListener != null) {
							onRefreshListener.loadMore();
						}

						/*if(handler!=null){
							handler.sendEmptyMessageDelayed(0, 2000);
						}*/
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

	}

	/**
	 * 添加加载尾
	 */
	private void initFoot() {
		View viewFooter = View.inflate(getContext(), R.layout.refresh_footer,
				null);

		refresh_footer_root = (LinearLayout) viewFooter
				.findViewById(R.id.refresh_footer_root);
		refresh_footer_root.measure(0, 0);
		footerHeight = refresh_footer_root.getMeasuredHeight();
		refresh_footer_root.setPadding(0, -footerHeight, 0, 0);

		this.addFooterView(viewFooter);
	}

	/**
	 * 添加刷新头,放在listview的顶部
	 */
	private void initHeader() {
		View view = View.inflate(getContext(), R.layout.refresh_header, null);
		// refresh_header_root 会是listview的第一个条目，包含刷新头和轮播图
		refresh_header_root = (LinearLayout) view
				.findViewById(R.id.refresh_header_root);
		// 刷新头对应的控件
		refresh_header_view = (LinearLayout) view
				.findViewById(R.id.refresh_header_view);

		refresh_header_progressbar = (ProgressBar) view
				.findViewById(R.id.refresh_header_progressbar);
		refresh_header_imageview = (ImageView) view
				.findViewById(R.id.refresh_header_imageview);

		refresh_header_text = (TextView) view
				.findViewById(R.id.refresh_header_text);
		refresh_header_time = (TextView) view
				.findViewById(R.id.refresh_header_time);

		// 测量刷新头的高度，用来设置它的padding，来控制它的位置
		refresh_header_view.measure(0, 0);
		refresh_header_height = refresh_header_view.getMeasuredHeight();

		// 设置刷新头默认隐藏
		refresh_header_view.setPadding(0, -refresh_header_height, 0, 0);

		// 添加view到listview
		this.addHeaderView(view);

		// 构建箭头的动画
		initAnimation();

	}

	/**
	 * 构建箭头的动画，以备使用
	 */
	private void initAnimation() {
		// 逆时针选择180度 箭头由下向上旋转
		rotateAnimationUp = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimationUp.setDuration(500);
		rotateAnimationUp.setFillAfter(true);

		// 逆时针选择180度 箭头由上向下选择
		rotateAnimationDown = new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimationDown.setDuration(500);
		rotateAnimationDown.setFillAfter(true);
	}

	/**
	 * 重写触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			// TODO 按下事件 根本没生效
			System.out.println("touch------down");
			// startY = (int) ev.getRawY();
			// System.out.println("down:startY:"+startY);

			// return true;
			break;
		case MotionEvent.ACTION_MOVE:
			// 为了安全起见，添加的一重验证
			// TODO 因为上面的按下事件根本没用，所以要在移动中重新获取
			if (startY == -1) {
				startY = (int) ev.getRawY();
			}
			int endY = (int) ev.getRawY();
			int dY = endY - startY;
			System.out.println("dY:" + dY);

			// 当前刷新头还隐藏的距离
			int padding = -refresh_header_height + dY;
			// 获取轮播图左上角的位置
			int[] locationCustomerView = new int[2];
			constomerView.getLocationInWindow(locationCustomerView);
			int constomerView_Y = locationCustomerView[1];
			// 获取listview左上角的位置
			int[] locationListView = new int[2];
			this.getLocationInWindow(locationListView);
			int listView_Y = locationListView[1];
			// 当padding大于刷新控件的高度的负数，
			// 并且轮播图的左上角大于等于listview的左上角的时候，开始执行判断逻辑
			// TODO 在判断执行刷新的时候，要刷新肯定是轮播图的Y在listview之下，才执行
			if (padding >= -refresh_header_height
					&& constomerView_Y >= listView_Y) {
				// 当刷新控件已经拉出来，并且当前状态是：下拉刷新时，更改当前状态位释放刷新。
				if (padding > 0 && currentState == PULL_REFRESH) {
					// 状态变为释放刷新，并调用设置当前状态的方法
					currentState = RELEASE_REFRESH;
					setCurrentOption();
				}
				// 当刷新控件又拖回去，并且当前状态是释放刷新时，更改当前状态位下拉刷新
				if (padding <= 0 && currentState == RELEASE_REFRESH) {
					currentState = PULL_REFRESH;
					setCurrentOption();
				}
				// 只有当padding>-refresh_header_height，
				// 才根据触摸事件move的情况，重新设置刷新控件的位置
				refresh_header_view.setPadding(0, padding, 0, 0);
				System.out.println("currentState:" + currentState);
				// 保证后续的事件去做响应，up事件做响应
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("touch------up");
			// 手指放开时，要根据当前状态判断要不要执行刷新操作
			if (currentState == RELEASE_REFRESH) {
				// 释放刷新状态，执行刷新操作
				// 更改当前状态为：正在刷新
				currentState = IS_REFRESHING;
				// 设置刷新头为完全显示
				refresh_header_view.setPadding(0, 0, 0, 0);
				// 更改当前状态的具体配置
				setCurrentOption();
				// 刷新逻辑，回调的方式，让调用者完成
				if (onRefreshListener != null) {
					onRefreshListener.pullDownRefresh();
				}
				/*if (handler != null) {
					// 模拟网络延迟
					handler.sendEmptyMessageDelayed(0, 3000);
				}*/
			} else {
				// 如果手指离开的时候，不是释放刷新状态，则弹回
				refresh_header_view.setPadding(0, -refresh_header_height, 0, 0);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据当前状态做具体的配置
	 */
	private void setCurrentOption() {
		switch (currentState) {
		case RELEASE_REFRESH:
			refresh_header_text.setText("释放刷新");
			refresh_header_imageview.setAnimation(rotateAnimationUp);
			break;
		case PULL_REFRESH:
			refresh_header_text.setText("下拉刷新");
			refresh_header_imageview.startAnimation(rotateAnimationDown);
			break;
		case IS_REFRESHING:
			refresh_header_text.setText("正在刷新");
			refresh_header_imageview.clearAnimation();
			refresh_header_imageview.setVisibility(View.GONE);
			refresh_header_progressbar.setVisibility(View.VISIBLE);
			refresh_header_time.setText(getTime());
			break;

		}
	}

	/**
	 * 获取当前的时间，有格式的返回
	 * 
	 * @return
	 */
	private CharSequence getTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return simpleDateFormat.format(date);
	}

	/**
	 * 刷新完的操作，善后
	 */
	public void finish() {
		// 刷新完毕后的业务逻辑
		if (currentState == IS_REFRESHING) {
			refresh_header_progressbar.setVisibility(View.INVISIBLE);
			refresh_header_imageview.setVisibility(View.VISIBLE);
			refresh_header_text.setText("下拉刷新");
			currentState = PULL_REFRESH;
			refresh_header_view.setPadding(0, -refresh_header_height, 0, 0);
		}

		// 加载完毕后的逻辑
		if (isload) {
			// 隐藏脚底
			refresh_footer_root.setPadding(0, -footerHeight, 0, 0);
			isload = false;
		}
	}

	/**
	 * 对外暴露一个方法，用来设置轮播图
	 * 
	 * @param view
	 *            轮播图view
	 */
	public void addCustomerView(View view) {
		constomerView = view;
		refresh_header_root.addView(constomerView);
	}

	/**
	 * 对外提供方法，传递刷新监听接口并实现其中的方法
	 * 
	 * @param onRefreshListener
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	/**
	 * 刷新监听接口，创建下拉和上拉方法；
	 * 
	 * @author rong
	 */
	public interface OnRefreshListener {
		// 下拉刷新方法
		public void pullDownRefresh();

		// 上拉加载方法
		public void loadMore();
	}

}
