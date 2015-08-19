package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.xgf.winecome.R;
import com.xgf.winecome.utils.UserInfoManager;

public class MoreActivity extends Activity implements OnClickListener {

	private RelativeLayout mOrderRl;
	private RelativeLayout mIntegralMallRl;
	private RelativeLayout mIntegralSearchRl;
	private RelativeLayout mIntegralExchangeRl;

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

		mOrderRl.setOnClickListener(this);
		mIntegralMallRl.setOnClickListener(this);
		mIntegralSearchRl.setOnClickListener(this);
		mIntegralExchangeRl.setOnClickListener(this);
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_order_rl: {
			Intent intent = new Intent(MoreActivity.this, OrderQueryActivity.class);
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
			Intent intent = new Intent(MoreActivity.this, IntegralMallActivity.class);
			startActivity(intent);

			break;
		}
		case R.id.more_integral_search_rl: {

			if (UserInfoManager.getIsMustAuth(getApplicationContext())) {
				Intent intent = new Intent(MoreActivity.this, IntegralQueryActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(MoreActivity.this, IntegralQueryResultActivity.class);
				startActivity(intent);
			}
			Intent intent = new Intent(MoreActivity.this, IntegralQueryActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.more_integral_exchange_rl: {
			Intent intent = new Intent(MoreActivity.this, IntegralOrderListActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

}
