package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
	private LinearLayout mBottomLl;
	private LinearLayout mInvoiceInfoLl;
	private LinearLayout mBottomDivLl;

	private RelativeLayout mInvoiceRl;
	private RelativeLayout mAreaRl;
	private RelativeLayout mTimeRl;
	private RelativeLayout mDateRl;

	private TextView mAreaTv;
	private TextView mTimeTv;
	private TextView mDateTv;
	private TextView mAreaTagTv;
	private TextView mTimeTagTv;
	private TextView mDateTagTv;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private EditText mDetailAreaEt;
	private EditText mInvoiceTitleEt;
	private EditText mInvoiceContentEt;

	private CheckBox mInvoiceCb;

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
		mBottomLl = (LinearLayout) findViewById(R.id.per_info_bottom_ll);
		mBottomDivLl = (LinearLayout) findViewById(R.id.per_info_bottom_division_ll);
		mInvoiceInfoLl = (LinearLayout) findViewById(R.id.per_info_invoice_info_ll);

		mAreaRl = (RelativeLayout) findViewById(R.id.per_info_area_rl);
		mTimeRl = (RelativeLayout) findViewById(R.id.per_info_time_rl);
		mDateRl = (RelativeLayout) findViewById(R.id.per_info_date_rl);
		mInvoiceRl = (RelativeLayout) findViewById(R.id.per_info_invoice_rl);

		mAreaTagTv = (TextView) findViewById(R.id.per_info_area_tag_tv);
		mDateTagTv = (TextView) findViewById(R.id.per_info_date_tag_tv);
		mTimeTagTv = (TextView) findViewById(R.id.per_info_time_tag_tv);

		mAreaTv = (TextView) findViewById(R.id.per_info_area_tv);
		mTimeTv = (TextView) findViewById(R.id.per_info_time_tv);
		mDateTv = (TextView) findViewById(R.id.per_info_date_tv);

		mPhoneEt = (EditText) findViewById(R.id.per_info_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.per_info_ver_code_et);
		mDetailAreaEt = (EditText) findViewById(R.id.per_info_detail_area_et);
		mInvoiceTitleEt = (EditText) findViewById(R.id.per_info_invoice_title_et);
		mInvoiceContentEt = (EditText) findViewById(R.id.per_info_invoice_content_et);

		mInvoiceCb = (CheckBox) findViewById(R.id.per_info_invoice_cb);
	}

	private void setUpListener() {
		mVerCodeLl.setOnClickListener(this);
		mSubmitLl.setOnClickListener(this);
		mInvoiceLl.setOnClickListener(this);
		mAreaRl.setOnClickListener(this);
		mTimeRl.setOnClickListener(this);
		mDateRl.setOnClickListener(this);
		mInvoiceRl.setOnClickListener(this);
		mAreaTv.setOnClickListener(this);
		mTimeTv.setOnClickListener(this);
		mDateTv.setOnClickListener(this);
		mAreaTagTv.setOnClickListener(this);
		mDateTagTv.setOnClickListener(this);
		mTimeTagTv.setOnClickListener(this);

		mPhoneEt.addTextChangedListener(this);
		mVerCodeEt.addTextChangedListener(this);
		mDetailAreaEt.addTextChangedListener(this);
		mInvoiceTitleEt.addTextChangedListener(this);
		mInvoiceContentEt.addTextChangedListener(this);

		mInvoiceCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (!isChecked) {
					mInvoiceInfoLl.setVisibility(View.GONE);
					mBottomDivLl.setVisibility(View.VISIBLE);
				} else {
					mInvoiceInfoLl.setVisibility(View.VISIBLE);
					mBottomDivLl.setVisibility(View.GONE);
				}

			}
		});

	}

	private void setUpData() {
	}

	private void updateShow() {
		if (TextUtils.isEmpty(mPhoneEt.getText().toString().trim())) {
			mPhoneEt.setError(getString(R.string.mobile_phone_hint));
		}
		if (TextUtils.isEmpty(mVerCodeEt.getText().toString().trim())) {
			mVerCodeEt.setError(getString(R.string.verification_code_hint));
		}
		if (TextUtils.isEmpty(mDetailAreaEt.getText().toString().trim())) {
			mDetailAreaEt.setError(getString(R.string.detail_info_hint));
		}
		if (TextUtils.isEmpty(mInvoiceTitleEt.getText().toString().trim())) {
			mInvoiceTitleEt.setError(getString(R.string.invoice_title_hint));
		}
		if (TextUtils.isEmpty(mInvoiceContentEt.getText().toString().trim())) {
			mInvoiceContentEt
					.setError(getString(R.string.invoice_content_hint));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 500: {
				mAreaTv.setText(data.getStringExtra("area"));
				break;
			}
			case 501: {
				mDateTv.setText(data.getStringExtra("date"));
				break;
			}
			case 502: {
				mTimeTv.setText(data.getStringExtra("time"));
				break;
			}
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		case R.id.per_info_area_tag_tv: {
			Intent intent = new Intent(PersonInfoActivity.this,
					AreaSelectActivity.class);
			startActivityForResult(intent, 500);
			break;
		}
		case R.id.per_info_date_rl:
		case R.id.per_info_date_tv:
		case R.id.per_info_date_tag_tv: {
			Intent intent = new Intent(PersonInfoActivity.this,
					DateSelectActivity.class);
			startActivityForResult(intent, 501);
			break;
		}
		case R.id.per_info_time_rl:
		case R.id.per_info_time_tv:
		case R.id.per_info_time_tag_tv: {
			Intent intent = new Intent(PersonInfoActivity.this,
					TimeSelectActivity.class);
			startActivityForResult(intent, 502);
			break;
		}
		default:
			break;
		}
	}
}
