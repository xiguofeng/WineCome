package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.ui.fragment.MainFragment;
import com.xgf.winecome.ui.fragment.MoreFragment;
import com.xgf.winecome.ui.fragment.ShopCartFragment;
import com.xgf.winecome.utils.CartManager;

public class HomeFragmentActivity extends FragmentActivity implements
		OnClickListener, OnPageChangeListener {
	private ViewPager viewPager;
	// 底部菜单图片
	private ImageView[] menusImageViews;
	// 引导图片资源
	private static final int[] menImgs = { R.drawable.all, R.drawable.near,
			R.drawable.map };
	// 记录当前选中位置
	private int currentIndex;

	private static LinearLayout mPayMenuLl;

	private static TextView mTotalMoneyTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mPayMenuLl = (LinearLayout) findViewById(R.id.main_pay_menu);
		mTotalMoneyTv = (TextView) findViewById(R.id.main_total_pay_tv);
		viewPager = (ViewPager) this.findViewById(R.id.pager);
		initial();
		initMenu();
		viewPager.setOnPageChangeListener(this);
	}

	private void initial() {
		menusImageViews = new ImageView[menImgs.length];
		List<Fragment> contents = new ArrayList<Fragment>();
		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				Fragment content = new MainFragment();
				contents.add(content);
			} else if (i == 1) {

				Fragment content = new ShopCartFragment();
				contents.add(content);

			} else if (i == 2) {
				Fragment content = new MoreFragment();
				contents.add(content);
			}
			// 新建Fragment的实例对象，并设置参数传递到Fragment中

		}
		// 这个getSupportFragmentManager只有activity继承FragmentActivity才会有
		MyFragmentPageAdapter adapter = new MyFragmentPageAdapter(
				getSupportFragmentManager(), contents);
		viewPager.setAdapter(adapter);
	}

	/**
	 * 初始化底部
	 */
	private void initMenu() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tabMenu);
		int menuCount = menImgs.length;
		// 循环取得小点图片
		for (int i = 0; i < menuCount; i++) {
			// 得到一个LinearLayout下面的每一个子元素
			RelativeLayout relativeLayout = (RelativeLayout) linearLayout
					.getChildAt(i);
			ImageView imageView = (ImageView) relativeLayout.getChildAt(0);
			menusImageViews[i] = imageView;
			// 默认都设为灰色
			imageView.setEnabled(true);
			// imageView.setOnClickListener(this);
			// 这里用布局来监听事件
			relativeLayout.setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			relativeLayout.setTag(i);
		}
		// 设置当面默认的位置
		currentIndex = 0;
		// 设置为白色，即选中状态
		menusImageViews[currentIndex].setEnabled(false);
	}

	private class MyFragmentPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> mContents;

		public MyFragmentPageAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		public MyFragmentPageAdapter(FragmentManager fm, List<Fragment> contents) {
			super(fm);
			mContents = contents;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return mContents.get(arg0);
		}

		@Override
		public int getCount() {
			return mContents.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// 设置底部小点选中状态
		setCurDot(position);

	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= menImgs.length) {
			return;
		}
		viewPager.setCurrentItem(position);
		if (0 == position) {
			if (CartManager.getsCartList().size() > 0) {
				showOrhHidePayBar(true);
			}
		} else {
			showOrhHidePayBar(false);
		}
	}

	/**
	 * 设置当前的小点的位置
	 */
	private void setCurDot(int position) {
		if (position < 0 || position > menImgs.length - 1
				|| currentIndex == position) {
			return;
		}
		menusImageViews[position].setEnabled(false);
		menusImageViews[currentIndex].setEnabled(true);

		currentIndex = position;

		if (0 == position) {
			if (CartManager.getsCartList().size() > 0) {
				showOrhHidePayBar(true);
			}
		} else {
			showOrhHidePayBar(false);
		}
	}

	public static void modifyOrderView(String totalPrice) {
		mTotalMoneyTv.setText(totalPrice);
	}

	public static void showOrhHidePayBar(boolean flag) {
		if (flag) {
			mPayMenuLl.setVisibility(View.VISIBLE);
		} else {
			mPayMenuLl.setVisibility(View.GONE);
		}
	}
}
