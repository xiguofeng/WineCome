package com.xgf.winecome.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xgf.winecome.R;
import com.xgf.winecome.ui.view.wheel.widget.OnWheelChangedListener;
import com.xgf.winecome.ui.view.wheel.widget.WheelView;
import com.xgf.winecome.ui.view.wheel.widget.adapters.ArrayWheelAdapter;

public class PersonInfoActivity extends BaseWheelActivity implements
		OnClickListener, OnWheelChangedListener {
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;

	private LinearLayout mVerCodeLl;
	private LinearLayout mSubmitLl;
	private LinearLayout mInvoiceLl;

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
		// setUpData();
	}

	private void setUpViews() {
		// mViewProvince = (WheelView) findViewById(R.id.id_province);
		// mViewCity = (WheelView) findViewById(R.id.id_city);
		// mViewDistrict = (WheelView) findViewById(R.id.id_district);
		// mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mVerCodeLl = (LinearLayout) findViewById(R.id.per_info_ver_code_ll);
		mSubmitLl = (LinearLayout) findViewById(R.id.per_info_submit_ll);
		mInvoiceLl= (LinearLayout) findViewById(R.id.per_info_invoice_ll);

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
		// // 添加change事件
		// mViewProvince.addChangingListener(this);
		// // 添加change事件
		// mViewCity.addChangingListener(this);
		// // 添加change事件
		// mViewDistrict.addChangingListener(this);
		// // 添加onclick事件
		// mBtnConfirm.setOnClickListener(this);

		mVerCodeLl.setOnClickListener(this);
		mSubmitLl.setOnClickListener(this);
		mInvoiceLl.setOnClickListener(this);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				PersonInfoActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.btn_confirm:
		// //showSelectedResult();
		// break;
		default:
			break;
		}
	}

}
