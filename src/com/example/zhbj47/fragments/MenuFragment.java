package com.example.zhbj47.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhbj47.MainActivity;
import com.example.zhbj47.R;
import com.example.zhbj47.adapter.HMAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MenuFragment extends BaseFragment {
	private static final String tag = "MenuFragment";
	private List<String> titleList = new ArrayList<String>();

	@ViewInject(R.id.lv_menu_news_center)
	private ListView lv_menu_news_center;
	private MyAdapter myAdapter;

	private int currentItem;

	@Override
	public void initData() {
	}

	@Override
	public View initUI() {
		View view = View.inflate(context, R.layout.layout_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

	public void initMenu(List<String> titleList) {
		this.titleList = titleList;
		Log.i(tag, "titleList.size() = " + titleList.size());

		if (myAdapter == null) {
			myAdapter = new MyAdapter(titleList, context);
			lv_menu_news_center.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}

		/**
		 * 左侧条目的点击事件
		 */
		lv_menu_news_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				currentItem = position;

				myAdapter.notifyDataSetChanged();

				// 调用NewCenterPager中的switchPager方法，切换内容页内容
				((MainActivity) context).getHomeFragment().getNewCenterPager()
						.switchPager(position);
				
				//内容页缩回操作
				slidingMenu.toggle();

			}
		});

	}

	/**
	 * 侧滑菜单的数据适配器
	 * 
	 * @author rong
	 */
	class MyAdapter extends HMAdapter {

		public MyAdapter(List list, Context context) {
			super(list, context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = View.inflate(context, R.layout.layout_item_menu,
						null);
				holder = new ViewHolder();
				holder.iv_menu_item = (ImageView) convertView
						.findViewById(R.id.iv_menu_item);
				holder.tv_menu_item = (TextView) convertView
						.findViewById(R.id.tv_menu_item);
				convertView.setTag(holder);
			}

			// 填充数据
			holder.tv_menu_item.setText(titleList.get(position));
			if (currentItem == position) {
				// 如果当前选中条目是要返回的条目
				holder.tv_menu_item.setTextColor(Color.RED);
				holder.iv_menu_item
						.setImageResource(R.drawable.menu_arr_select);
				convertView
						.setBackgroundResource(R.drawable.menu_item_bg_select);
			} else {
				holder.tv_menu_item.setTextColor(Color.WHITE);
				holder.iv_menu_item
						.setImageResource(R.drawable.menu_arr_normal);
				convertView.setBackgroundResource(R.drawable.transparent);
			}
			return convertView;
		}
	}

	public static class ViewHolder {
		ImageView iv_menu_item;
		TextView tv_menu_item;
	}
}
