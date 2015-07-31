package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xgf.winecome.R;
import com.xgf.winecome.qrcode.google.zxing.client.CaptureActivity;

public class OrderStateActivity extends Activity implements OnClickListener {

	private ImageView mBackIv;

	private ImageView mQrcodeIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_state);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mBackIv = (ImageView) findViewById(R.id.order_state_back_iv);
		mQrcodeIv = (ImageView) findViewById(R.id.order_state_qr_iv);
	}

	private void setUpListener() {
		mQrcodeIv.setOnClickListener(this);
		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_state_qr_iv: {
			Intent intent = new Intent(OrderStateActivity.this,
					CaptureActivity.class);
			startActivity(intent);
			break;
		}

		default:
			break;
		}
	}

}
