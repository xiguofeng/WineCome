package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.ui.view.wheel.widget.OnWheelChangedListener;
import com.xgf.winecome.ui.view.wheel.widget.WheelView;
import com.xgf.winecome.ui.view.wheel.widget.adapters.ArrayWheelAdapter;
import com.xgf.winecome.utils.DateUtils;
import com.xgf.winecome.utils.TimeUtils;

public class TimeSelectActivity extends Activity implements OnClickListener,
		OnWheelChangedListener {
	private TextView mCancelTv;
	private TextView mConfirmTv;

	private WheelView mViewHours;
	private WheelView mViewMinutes;

	private ArrayWheelAdapter<String> hoursAdapter;
	private ArrayWheelAdapter<String> minutesAdapter;

	private String[] mHours = new String[24];
	private String[] mInutes = new String[60];

	int currentMonth = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_select);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.9
		// p.alpha = 1.0f; // 设置本身透明度
		p.dimAmount = 0.6f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setAttributes(p);

		mViewHours = (WheelView) findViewById(R.id.id_hours);
		mViewMinutes = (WheelView) findViewById(R.id.id_minutes);

		mCancelTv = (TextView) findViewById(R.id.time_select_cance_tv);
		mConfirmTv = (TextView) findViewById(R.id.time_select_confirm_tv);
	}

	private void setUpListener() {
		// 添加change事件
		mViewHours.addChangingListener(this);
		// 添加change事件
		mViewMinutes.addChangingListener(this);
		// 添加change事件
		// 添加onclick事件
		mConfirmTv.setOnClickListener(this);
		mCancelTv.setOnClickListener(this);
	}

	private void setUpData() {
		// 设置可见条目数量
		mViewHours.setVisibleItems(7);
		mViewMinutes.setVisibleItems(7);
		setData();
	}

	private void setData() {
		for (int i = 0; i < mHours.length; i++) {
			mHours[i] = i + " 时";
		}
		for (int i = 0; i < mInutes.length; i++) {
			mInutes[i] = i + " 分";
		}

		hoursAdapter = new ArrayWheelAdapter<String>(TimeSelectActivity.this,
				mHours);
		minutesAdapter = new ArrayWheelAdapter<String>(TimeSelectActivity.this,
				mInutes);
		mViewHours.setViewAdapter(hoursAdapter);
		mViewHours.setCurrentItem(getNowHours());
		mViewHours.setCyclic(true);
		mViewMinutes.setViewAdapter(minutesAdapter);
		mViewMinutes.setCurrentItem(getNowMinutes());
		mViewMinutes.setCyclic(true);

	}

	/**
	 * 获取当前日期的分钟的位置
	 * 
	 * @return
	 */
	private int getNowMinutes() {
		int position = 0;
		String today = getToday();
		String minutes = today.substring(14, 16);
		if (minutes.startsWith("0")) {
			minutes = minutes.substring(1);
		}
		Log.e("xxx_time_minutes", minutes);
		minutes = minutes + " 分";
		for (int i = 0; i < mInutes.length; i++) {
			if (minutes.equals(mInutes[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 获取当天的小时
	 * 
	 * @return
	 */
	private int getNowHours() {
		int position = 0;
		String today = getToday();
		String hours = today.substring(11, 13);
		Log.e("xxx_time_today", today);
		Log.e("xxx_time_hour", hours);
		hours = hours + " 时";
		for (int i = 0; i < mHours.length; i++) {
			if (hours.equals(mHours[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 获取今天的日期
	 * 
	 * @return
	 */
	private String getToday() {
		String str = TimeUtils.TimeStamp2Date(
				String.valueOf(System.currentTimeMillis()),
				TimeUtils.FORMAT_PATTERN_DATE);
		return str;
	}

	/**
	 * 获取选择的日期的值
	 * 
	 * @return
	 */
	public String getSelectedTime() {
		return mHours[mViewHours.getCurrentItem()].toString().trim() + ":"
				+ mInutes[mViewMinutes.getCurrentItem()].toString().trim();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		String trim = null;
		switch (wheel.getId()) {
		case R.id.id_year:
			trim = DateUtils.splitDateString(
					mHours[mViewHours.getCurrentItem()].toString()).trim();
			break;
		case R.id.id_month:
			trim = DateUtils.splitDateString(
					mInutes[mViewMinutes.getCurrentItem()].toString()).trim();
			currentMonth = Integer.parseInt(trim);

			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time_select_cance_tv: {
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
		case R.id.time_select_confirm_tv: {
			Intent intent = new Intent();
			intent.putExtra("time", getSelectedTime());
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		default:
			break;
		}
	}

}
