package com.xgf.winecome.ui.activity;


import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.utils.CartManager;

public class HomeActivity extends TabActivity implements
		android.view.View.OnClickListener {

	public static final String TAG = HomeActivity.class.getSimpleName();

	public static final String TAB_MAIN = "MAIN";
	public static final String TAB_CART = "CART";
	public static final String TAB_MORE = "MORE";

	private RadioGroup mTabButtonGroup;

	private static TabHost mTabHost;

	private static LinearLayout mPayMenuLl;

	private static TextView mTotalMoneyTv;

	private LinearLayout mBuyLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		findViewById();
		initView();
		initData();
	}

	private void findViewById() {
		mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);
		mPayMenuLl = (LinearLayout) findViewById(R.id.home_pay_menu);
		mTotalMoneyTv = (TextView) findViewById(R.id.home_total_pay_tv);
		mBuyLl = (LinearLayout) findViewById(R.id.home_buy_ll);
		mBuyLl.setOnClickListener(this);
	}

	private void initView() {

		mTabHost = getTabHost();

		Intent i_home = new Intent(this, MainActivity.class);
		Intent i_cart = new Intent(this, ShopCartActivity.class);
		Intent i_more = new Intent(this, MoreActivity.class);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
				.setContent(i_home));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_CART).setIndicator(TAB_CART)
				.setContent(i_cart));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_MORE).setIndicator(TAB_MORE)
				.setContent(i_more));

		Log.e("xxx_", "mTabHost.setCurrentTabByTag(TAB_MORE);");
		mTabHost.setCurrentTabByTag(TAB_MAIN);

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.home_tab_home_rb:
							mTabHost.setCurrentTabByTag(TAB_MAIN);
							if (CartManager.sCartList.size() > 0) {
								showOrhHidePayBar(true);
							} else {
								showOrhHidePayBar(false);
							}
							break;

						case R.id.home_tab_cart_rb:
							mTabHost.setCurrentTabByTag(TAB_CART);
							showOrhHidePayBar(false);
							break;

						case R.id.home_tab_more_rb:
							mTabHost.setCurrentTabByTag(TAB_MORE);
							showOrhHidePayBar(false);
							break;

						default:
							break;
						}
					}
				});
	}

	private void initData() {
		// mTabHost.setCurrentTabByTag(TAB_MAIN);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	protected void exitApp() {
		showAlertDialog("退出程序", "确定退出？", "确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}, "取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	protected void showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
	}

	public static void modifyPayView(String totalPrice) {
		mTotalMoneyTv.setText(totalPrice);
	}

	public static void showOrhHidePayBarByCart(boolean flag) {
		
		mTabHost.setCurrentTabByTag(TAB_MAIN);
		if (flag) {
			mPayMenuLl.setVisibility(View.VISIBLE);
		} else {
			mPayMenuLl.setVisibility(View.GONE);
		}
	}

	public static void showOrhHidePayBar(boolean flag) {
		if (flag) {
			mPayMenuLl.setVisibility(View.VISIBLE);
		} else {
			mPayMenuLl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_buy_ll: {
			Intent intent = new Intent(HomeActivity.this,
					PersonInfoActivity.class);
			// intent.setAction(LoginActivity.ORIGIN_FROM_ORDER_KEY);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		default:
			break;
		}

	}

}
