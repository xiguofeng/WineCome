package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xgf.winecome.R;

public class IntegralQueryActivity extends Activity implements OnClickListener{
	private LinearLayout mQueryLl;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_query);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mQueryLl = (LinearLayout) findViewById(R.id.order_query_submit_ll);

		mPhoneEt = (EditText) findViewById(R.id.order_query_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.order_query_ver_code_et);
	}

	private void setUpListener() {
		mQueryLl.setOnClickListener(this);

	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_query_submit_ll: {
			Intent intent = new Intent(IntegralQueryActivity.this,
					OrderListActivity.class);
			startActivity(intent);
			break;
		}

		default:
			break;
		}
	}

}
