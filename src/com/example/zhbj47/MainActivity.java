package com.example.zhbj47;

import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.zhbj47.fragments.HomeFragment;
import com.example.zhbj47.fragments.MenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content);

		setBehindContentView(R.layout.menu_frame);
		slidingMenu = getSlidingMenu();
		slidingMenu.setBehindWidthRes(R.dimen.slidingmenu_offset);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		MenuFragment menuFragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu, menuFragment, "MENU").commit();

		HomeFragment homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, homeFragment, "HOME").commit();

	}

	// 通过tag去获取已有的对象  MenuFragment
	public MenuFragment getMenuFragment() {
		return (MenuFragment) getSupportFragmentManager().findFragmentByTag(
				"MENU");
	}
	// 通过tag去获取已有的对象  HomeFragment
	public HomeFragment getHomeFragment() {
		return (HomeFragment) getSupportFragmentManager().findFragmentByTag(
				"HOME");
	}

}
