package com.xgf.winecome.ui.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
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

	private static RelativeLayout mPayMenuRl;

	private static LinearLayout mMainPayMenuLl;

	private static LinearLayout mCartPayMenuLl;

	private static LinearLayout mCartBuyLl;

	private static TextView mMainTotalMoneyTv;

	private static TextView mCartTotalMoneyTv;

	private static TextView mCartTotalNumTv;

	private CheckBox mCheckAllIb;

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
		mPayMenuRl = (RelativeLayout) findViewById(R.id.home_pay_menu_rl);
		mMainPayMenuLl = (LinearLayout) findViewById(R.id.home_main_pay_menu);
		mCartPayMenuLl = (LinearLayout) findViewById(R.id.home_cart_pay_menu);
		mMainTotalMoneyTv = (TextView) findViewById(R.id.home_main_total_pay_tv);
		mCartTotalMoneyTv = (TextView) findViewById(R.id.home_cart_total_pay_tv);
		mCartTotalNumTv = (TextView) findViewById(R.id.home_cart_total_num_tv);
		mBuyLl = (LinearLayout) findViewById(R.id.home_main_buy_ll);
		mBuyLl.setOnClickListener(this);
		mCartBuyLl = (LinearLayout) findViewById(R.id.home_cart_buy_ll);
		mCartBuyLl.setOnClickListener(this);
		mCheckAllIb = (CheckBox) findViewById(R.id.home_cart_pay_ib);
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

		mTabHost.setCurrentTabByTag(TAB_MAIN);

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.home_tab_home_rb:
							mTabHost.setCurrentTabByTag(TAB_MAIN);
							showOrHideCartPayBar(false);
							mCheckAllIb.setChecked(false);
							if (CartManager.getsCartList().size() > 0) {
								showOrhHideMainPayBar(true);
							} else {
								showOrhHideMainPayBar(false);
							}
							break;

						case R.id.home_tab_cart_rb:
							mTabHost.setCurrentTabByTag(TAB_CART);
							showOrhHideMainPayBar(false);
							showOrHideCartPayBar(true);
							break;

						case R.id.home_tab_more_rb:
							mTabHost.setCurrentTabByTag(TAB_MORE);
							showOrhHideMainPayBar(false);
							showOrHideCartPayBar(false);
							break;

						default:
							break;
						}
					}
				});

		mCheckAllIb
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						ShopCartActivity.refreshView(isChecked);

						CartManager.getsSelectCartList().clear();
						if (isChecked) {
							CartManager.getsSelectCartList().addAll(
									CartManager.getsCartList());
						}
						CartManager.setCartTotalMoney();
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

	public static void modifyMainPayView(String totalPrice, boolean isShow) {
		mMainTotalMoneyTv.setText(totalPrice);
		if (isShow && Double.parseDouble(totalPrice) > 0) {
			showOrhHideMainPayBar(true);
		}
	}

	public static void modifyCartPayView(String totalPrice, String totalNum) {
		mCartTotalMoneyTv.setText(totalPrice);
		mCartTotalNumTv.setText("(" + totalNum + ")");
	}

	public static void showOrhHideMainPayBar(boolean flag) {
		mPayMenuRl.setVisibility(View.VISIBLE);
		if (flag && mTabHost.getCurrentTabTag().endsWith(TAB_MAIN)) {
			mMainPayMenuLl.setVisibility(View.VISIBLE);
			mCartPayMenuLl.setVisibility(View.GONE);
		} else {
			mPayMenuRl.setVisibility(View.GONE);
		}
	}

	public static void showOrHideCartPayBar(boolean flag) {
		mPayMenuRl.setVisibility(View.VISIBLE);
		if (flag && mTabHost.getCurrentTabTag().endsWith(TAB_CART)) {
			mMainPayMenuLl.setVisibility(View.GONE);
			mCartPayMenuLl.setVisibility(View.VISIBLE);
		} else {
			mPayMenuRl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_main_buy_ll: {
			if (CartManager.getsCartList().size() > 0) {
				Intent intent = new Intent(HomeActivity.this,
						PersonInfoActivity.class);
				intent.setAction(PersonInfoActivity.ORIGIN_FROM_MAIN_ACTION);
				// intent.setAction(LoginActivity.ORIGIN_FROM_ORDER_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
			break;
		}
		case R.id.home_cart_buy_ll: {
			if (CartManager.getsSelectCartList().size() > 0) {
				Intent intent = new Intent(HomeActivity.this,
						PersonInfoActivity.class);
				intent.setAction(PersonInfoActivity.ORIGIN_FROM_CART_ACTION);
				// intent.setAction(LoginActivity.ORIGIN_FROM_ORDER_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {

			}
			break;
		}

		default:
			break;
		}

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

}
