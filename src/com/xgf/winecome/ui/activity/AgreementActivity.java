package com.xgf.winecome.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgf.winecome.R;

public class AgreementActivity extends BaseActivity implements OnClickListener {

	private ImageView mBackIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		mBackIv = (ImageView) findViewById(R.id.agreement_back_iv);
	}

	@Override
	protected void initView() {
		mBackIv.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}

	}

}
