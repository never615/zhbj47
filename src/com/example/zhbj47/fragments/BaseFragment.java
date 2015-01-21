package com.example.zhbj47.fragments;

import com.example.zhbj47.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public View view;
	public  Context context;
	public SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity();
		super.onCreate(savedInstanceState);
		slidingMenu = ((MainActivity)context).getSlidingMenu();
	}
	
	/**
	 * 初始化UI
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initUI();
		return view;
	}
	

	/**
	 * 填充数据
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * 初始化界面
	 * @return
	 */
	public abstract View initUI();
	/**
	 * 填充数据
	 */
	public abstract void initData();
	
}
