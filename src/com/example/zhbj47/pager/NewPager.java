package com.example.zhbj47.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhbj47.R;
import com.example.zhbj47.bean.NewCenter.NewCenterItem;
import com.example.zhbj47.view.pagerindicator.TabPageIndicator;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 新闻页四个子页面（新闻，专题。。。）
 * @author rong
 *
 */
public class NewPager extends BasePager {

	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;

	@ViewInject(R.id.pager)
	private ViewPager pager;

	private List<BasePager> pagerList;
	private MyPagerAdapter myPagerAdapter;

	// 将填充页面能提供数据的对象传递进来，通过json解析得到的。
	private NewCenterItem newCenterItem;

	public NewPager(Context context, NewCenterItem newCenterItem) {
		super(context);
		this.newCenterItem = newCenterItem;
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.frag_news, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		pagerList=new ArrayList<BasePager>();
		pagerList.clear();

		//准备数据，然后填充到viewPager
		for (int i = 0; i < newCenterItem.children.size(); i++) {
			pagerList.add(new NewsCenterItemPager(context,
					newCenterItem.children.get(i).url));
			
		}
		if(myPagerAdapter==null){
			myPagerAdapter=new MyPagerAdapter();
			pager.setAdapter(myPagerAdapter);
		}else{
			myPagerAdapter.notifyDataSetChanged();
		}
		
		//绑定指针和viewPager
		indicator.setViewPager(pager);
		
		//防止在任何时候都能左滑，设置选中页面的监听，只有第一个item的时候，可以左滑
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(arg0==0){
					//第一个，可以左滑
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}else{
					//其他，不可以左滑
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
				//处理bug，指针不会走，手动设置
				indicator.setCurrentItem(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		

	}
	
	class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pagerList.get(position).getRootView());
			pagerList.get(position).initData();
			return pagerList.get(position).getRootView();
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		//指定对应指针的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return newCenterItem.children.get(position).title;
		}
	}

}
