package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xgf.winecome.R;

public class PersonInfoActivity extends Activity implements OnClickListener,
		TextWatcher {

	private LinearLayout mVerCodeLl;
	private LinearLayout mSubmitLl;
	private LinearLayout mInvoiceLl;

	private RelativeLayout mAreaRl;
	private RelativeLayout mTimeRl;
	private RelativeLayout mDateRl;

	private TextView mAreaTv;
	private TextView mTimeTv;
	private TextView mDateTv;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private EditText mAreaEt;
	private EditText mDetailAreaEt;
	private EditText mDateEt;
	private EditText mTimeEt;
	private EditText mInvoiceTitleEt;
	private EditText mInvoiceContentEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info_form);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mVerCodeLl = (LinearLayout) findViewById(R.id.per_info_ver_code_ll);
		mSubmitLl = (LinearLayout) findViewById(R.id.per_info_submit_ll);
		mInvoiceLl = (LinearLayout) findViewById(R.id.per_info_invoice_ll);

		mAreaRl = (RelativeLayout) findViewById(R.id.per_info_area_rl);
		mTimeRl = (RelativeLayout) findViewById(R.id.per_info_time_rl);
		mDateRl = (RelativeLayout) findViewById(R.id.per_info_date_rl);

		mAreaTv = (TextView) findViewById(R.id.per_info_area_tv);
		mTimeTv = (TextView) findViewById(R.id.per_info_time_tv);
		mDateTv = (TextView) findViewById(R.id.per_info_date_tv);

		mPhoneEt = (EditText) findViewById(R.id.per_info_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.per_info_ver_code_et);
		mAreaEt = (EditText) findViewById(R.id.per_info_area_et);
		mDetailAreaEt = (EditText) findViewById(R.id.per_info_detail_area_et);
		mDateEt = (EditText) findViewById(R.id.per_info_date_et);
		mTimeEt = (EditText) findViewById(R.id.per_info_time_et);
		mInvoiceTitleEt = (EditText) findViewById(R.id.per_info_invoice_title_et);
		mInvoiceContentEt = (EditText) findViewById(R.id.per_info_invoice_content_et);
	}

	private void setUpListener() {
		mVerCodeLl.setOnClickListener(this);
		mSubmitLl.setOnClickListener(this);
		mInvoiceLl.setOnClickListener(this);
		mAreaRl.setOnClickListener(this);
		mTimeRl.setOnClickListener(this);
		mDateRl.setOnClickListener(this);
		mAreaTv.setOnClickListener(this);
		mTimeTv.setOnClickListener(this);
		mDateTv.setOnClickListener(this);
	}

	private void setUpData() {
	}

	private void updateShow() {
		mVerCodeEt.setError("");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		updateShow();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.per_info_area_rl:
		case R.id.per_info_area_tv:
		case R.id.per_info_area_et: {
			Intent intent = new Intent(PersonInfoActivity.this,
					AreaSelectActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.per_info_date_rl:
		case R.id.per_info_date_tv:
		case R.id.per_info_date_et: {
			Intent intent = new Intent(PersonInfoActivity.this,
					AreaSelectActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.per_info_time_rl:
		case R.id.per_info_time_tv:
		case R.id.per_info_time_et: {
			Intent intent = new Intent(PersonInfoActivity.this,
					DateWheelActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

}
