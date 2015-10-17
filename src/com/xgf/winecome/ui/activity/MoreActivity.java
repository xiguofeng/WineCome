package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.qrcode.google.zxing.client.CaptureActivity;
import com.xgf.winecome.ui.view.widget.ActionSheetDialog;
import com.xgf.winecome.ui.view.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.xgf.winecome.ui.view.widget.ActionSheetDialog.SheetItemColor;

public class MoreActivity extends Activity implements OnClickListener {

	private RelativeLayout mOrderRl;
	private RelativeLayout mIntegralMallRl;
	private RelativeLayout mIntegralSearchRl;
	private RelativeLayout mIntegralExchangeRl;
	private RelativeLayout mQrCodeRl;
	private RelativeLayout mCustomerServiceRl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		initView();
		initData();
	}

	private void initView() {
		mOrderRl = (RelativeLayout) findViewById(R.id.more_order_rl);
		mIntegralMallRl = (RelativeLayout) findViewById(R.id.more_integral_shop_rl);
		mIntegralSearchRl = (RelativeLayout) findViewById(R.id.more_integral_search_rl);
		mIntegralExchangeRl = (RelativeLayout) findViewById(R.id.more_integral_exchange_rl);
		mQrCodeRl = (RelativeLayout) findViewById(R.id.more_qrcode_rl);
		mCustomerServiceRl = (RelativeLayout) findViewById(R.id.more_customer_service_rl);

		mOrderRl.setOnClickListener(this);
		mIntegralMallRl.setOnClickListener(this);
		mIntegralSearchRl.setOnClickListener(this);
		mIntegralExchangeRl.setOnClickListener(this);
		mQrCodeRl.setOnClickListener(this);
		mCustomerServiceRl.setOnClickListener(this);
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_order_rl: {
			Intent intent = new Intent(MoreActivity.this,
					OrderQueryActivity.class);
			startActivity(intent);
			// if (UserInfoManager.getIsMustAuth(getApplicationContext())) {
			//
			// } else {
			// Intent intent = new Intent(MoreActivity.this,
			// OrderListActivity.class);
			// startActivity(intent);
			// }
			break;
		}
		case R.id.more_integral_shop_rl: {
			Intent intent = new Intent(MoreActivity.this,
					IntegralMallActivity.class);
			startActivity(intent);

			break;
		}
		case R.id.more_integral_search_rl: {
			Intent intent = new Intent(MoreActivity.this,
					IntegralQueryActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.more_integral_exchange_rl: {
			Intent intent = new Intent(MoreActivity.this,
					MainActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.more_qrcode_rl: {
			Intent intent = new Intent(MoreActivity.this, CaptureActivity.class);
			intent.setAction(CaptureActivity.ORIGIN_FROM_MORE_ACTION);
			startActivity(intent);
			break;
		}
		case R.id.more_customer_service_rl: {
			new ActionSheetDialog(MoreActivity.this)
					.builder()
					.setTitle("拨打酒来了客服热线")
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem("025-86883575", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									Intent intent = new Intent(
											Intent.ACTION_CALL, Uri
													.parse("tel:025-86883575"));
									startActivity(intent);
								}
							})
					.addSheetItem("025-86883475", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									Intent intent = new Intent(
											Intent.ACTION_CALL, Uri
													.parse("tel:025-86883475"));
									startActivity(intent);
								}
							}).show();
			break;
		}

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			HomeActivity.showMainByOnkey();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
