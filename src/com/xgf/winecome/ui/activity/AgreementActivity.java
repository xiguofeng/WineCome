package com.xgf.winecome.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xgf.winecome.R;

public class AgreementActivity extends BaseActivity implements OnClickListener {

	private ImageView mBackIv;

	// private CustomTextView mContentTv;

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
		// mContentTv = (CustomTextView)
		// findViewById(R.id.agreement_content_tv);
	}

	@Override
	protected void initView() {
		mBackIv.setOnClickListener(this);
		// mContentTv.setText(getString(R.string.agreement_content));
		// mContentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
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
