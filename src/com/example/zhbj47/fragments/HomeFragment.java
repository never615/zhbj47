package com.example.zhbj47.fragments;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.zhbj47.R;
import com.example.zhbj47.pager.BasePager;
import com.example.zhbj47.pager.FunctionPager;
import com.example.zhbj47.pager.GovAffarisPager;
import com.example.zhbj47.pager.NewCenterPager;
import com.example.zhbj47.pager.SettingPager;
import com.example.zhbj47.pager.SmartServicePager;
import com.example.zhbj47.view.LazyViewPager.OnPageChangeListener;
import com.example.zhbj47.view.MyViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeFragment extends BaseFragment {

	@ViewInject(R.id.layout_content)
	private MyViewPager layout_content;

	@ViewInject(R.id.main_radio)
	private RadioGroup main_radio;

	private List<BasePager> pagerList;

	@Override
	public View initUI() {
		View view = View.inflate(context, R.layout.frag_home, null);
		ViewUtils.inject(this, view);

		return view;
	}

	@Override
	public void initData() {
		pagerList = new ArrayList<BasePager>();

		pagerList.add(new FunctionPager(context));
		pagerList.add(new NewCenterPager(context));
		pagerList.add(new SmartServicePager(context));
		pagerList.add(new GovAffarisPager(context));
		pagerList.add(new SettingPager(context));

		layout_content.setAdapter(new MyPagerAdapter());

		/**
		 * 底部按钮的选中事件监听
		 */
		main_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_function:
					layout_content.setCurrentItem(0);
					break;
				case R.id.rb_news_center:
					layout_content.setCurrentItem(1);
					break;
				case R.id.rb_smart_service:
					layout_content.setCurrentItem(2);
					break;
				case R.id.rb_gov_affairs:
					layout_content.setCurrentItem(3);
					break;
				case R.id.rb_setting:
					layout_content.setCurrentItem(4);
					break;
				}
			}
		});
		// 默认选中首页
		main_radio.check(R.id.rb_function);

		/**
		 * 页面变换调用的方法
		 */
		layout_content.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * 页面选中的时候调用的方法
			 */
			@Override
			public void onPageSelected(int position) {
				pagerList.get(position).initData();
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 返回NewCenterPager对象的方法
	 * 
	 * @return
	 */
	public NewCenterPager getNewCenterPager() {
		return (NewCenterPager) pagerList.get(1);
	}

	/**
	 * viewpager的适配器
	 * 
	 * @author rong
	 * 
	 */
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pagerList.get(position).getRootView());
			return pagerList.get(position).getRootView();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
