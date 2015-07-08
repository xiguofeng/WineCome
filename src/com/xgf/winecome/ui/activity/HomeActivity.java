package com.xgf.winecome.ui.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.xgf.winecome.R;

public class HomeActivity extends TabActivity {

	public static final String TAG = HomeActivity.class.getSimpleName();

	public static final String TAB_MAIN = "MAIN";
	public static final String TAB_CART = "CART";
	public static final String TAB_MORE = "MORE";

	private RadioGroup mTabButtonGroup;

	private TabHost mTabHost;

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
							break;

						case R.id.home_tab_cart_rb:
							mTabHost.setCurrentTabByTag(TAB_CART);
							break;

						case R.id.home_tab_more_rb:
							mTabHost.setCurrentTabByTag(TAB_MORE);
							break;

						default:
							break;
						}
					}
				});
	}
	
	private void initData(){
		//mTabHost.setCurrentTabByTag(TAB_MAIN);
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

}
