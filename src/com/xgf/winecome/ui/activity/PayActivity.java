package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.xgf.winecome.R;

public class PayActivity extends Activity implements OnClickListener {

	private RelativeLayout mCashRl;
	private RelativeLayout mPosRl;
	private RelativeLayout mAlipayRl;
	private RelativeLayout mWeChatRl;
	private RelativeLayout mUnionpayRl;

	private CheckBox mCashCb;
	private CheckBox mPosCb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mCashRl = (RelativeLayout) findViewById(R.id.pay_cash_rl);
		mPosRl = (RelativeLayout) findViewById(R.id.pay_pos_rl);
		mAlipayRl = (RelativeLayout) findViewById(R.id.pay_alipay_rl);
		mWeChatRl = (RelativeLayout) findViewById(R.id.pay_wechat_rl);
		mUnionpayRl = (RelativeLayout) findViewById(R.id.pay_unionpay_rl);

		mCashCb = (CheckBox) findViewById(R.id.pay_cash_cb);
		mPosCb = (CheckBox) findViewById(R.id.pay_pos_cb);
	}

	private void setUpListener() {

		mPosRl.setOnClickListener(this);
		mAlipayRl.setOnClickListener(this);
		mWeChatRl.setOnClickListener(this);
		mCashRl.setOnClickListener(this);
		mUnionpayRl.setOnClickListener(this);
		mCashCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

			}
		});
		mPosCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

			}
		});

	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.pay_alipay_rl: {
			Intent intent = new Intent(PayActivity.this,
					AreaSelectActivity.class);
			startActivityForResult(intent, 500);
			break;
		}
		case R.id.pay_wechat_rl: {
			Intent intent = new Intent(PayActivity.this,
					DateSelectActivity.class);
			startActivityForResult(intent, 501);
			break;
		}
		case R.id.pay_unionpay_rl: {
			Intent intent = new Intent(PayActivity.this,
					TimeSelectActivity.class);
			startActivityForResult(intent, 502);
			break;
		}
		case R.id.per_info_invoice_rl: {
			break;
		}
		default:
			break;
		}
	}

}
