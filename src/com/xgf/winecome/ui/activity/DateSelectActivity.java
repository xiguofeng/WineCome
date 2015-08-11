package com.xgf.winecome.ui.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.ui.view.wheel.widget.OnWheelChangedListener;
import com.xgf.winecome.ui.view.wheel.widget.WheelView;
import com.xgf.winecome.ui.view.wheel.widget.adapters.ArrayWheelAdapter;
import com.xgf.winecome.utils.DateUtils;
import com.xgf.winecome.utils.TimeUtils;

public class DateSelectActivity extends Activity implements OnClickListener,
		OnWheelChangedListener {
	private TextView mCancelTv;
	private TextView mConfirmTv;

	private WheelView mViewYear;
	private WheelView mViewMonth;
	private WheelView mViewDay;

	private ArrayWheelAdapter<String> yearsAdapter;
	private ArrayWheelAdapter<String> monthsAdapter;
	private ArrayWheelAdapter<String> tinyDaysAdapter;
	private ArrayWheelAdapter<String> smallDaysAdapter;
	private ArrayWheelAdapter<String> bigDaysAdapter;
	private ArrayWheelAdapter<String> normalDaysAdapter;

	private String[] years = new String[100];
	private String[] months = new String[12];
	private String[] tinyDays = new String[28];
	private String[] smallDays = new String[29];
	private String[] normalDays = new String[30];
	private String[] bigDays = new String[31];

	int currentMonth = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_select);
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

		mViewYear = (WheelView) findViewById(R.id.id_year);
		mViewMonth = (WheelView) findViewById(R.id.id_month);
		mViewDay = (WheelView) findViewById(R.id.id_day);

		mCancelTv = (TextView) findViewById(R.id.date_select_cance_tv);
		mConfirmTv = (TextView) findViewById(R.id.date_select_confirm_tv);
	}

	private void setUpListener() {
		// 添加change事件
		mViewYear.addChangingListener(this);
		// 添加change事件
		mViewMonth.addChangingListener(this);
		// 添加change事件
		mViewDay.addChangingListener(this);
		// 添加onclick事件
		mConfirmTv.setOnClickListener(this);
		mCancelTv.setOnClickListener(this);
	}

	private void setUpData() {
		// 设置可见条目数量
		mViewYear.setVisibleItems(7);
		mViewMonth.setVisibleItems(7);
		mViewDay.setVisibleItems(7);
		setData();
	}

	private void setData() {
		for (int i = 0; i < years.length; i++) {
			years[i] = 1960 + i + " 年";
		}
		for (int i = 0; i < months.length; i++) {
			if (i < 9) {
				months[i] = "0" + (1 + i) + " 月";
			} else {
				months[i] = (1 + i) + " 月";
			}
		}
		for (int i = 0; i < tinyDays.length; i++) {
			if (i < 9) {
				tinyDays[i] = "0" + (1 + i) + " 日";
			} else {
				tinyDays[i] = (1 + i) + " 日";
			}
		}
		for (int i = 0; i < smallDays.length; i++) {
			if (i < 9) {
				smallDays[i] = "0" + (1 + i) + " 日";
			} else {
				smallDays[i] = (1 + i) + " 日";
			}
		}
		for (int i = 0; i < normalDays.length; i++) {
			if (i < 9) {
				normalDays[i] = "0" + (1 + i) + " 日";
			} else {
				normalDays[i] = (1 + i) + " 日";
			}
		}
		for (int i = 0; i < bigDays.length; i++) {
			if (i < 9) {
				bigDays[i] = "0" + (1 + i) + " 日";
			} else {
				bigDays[i] = (1 + i) + " 日";
			}
		}
		yearsAdapter = new ArrayWheelAdapter<String>(DateSelectActivity.this,
				years);
		monthsAdapter = new ArrayWheelAdapter<String>(DateSelectActivity.this,
				months);
		tinyDaysAdapter = new ArrayWheelAdapter<String>(
				DateSelectActivity.this, tinyDays);
		smallDaysAdapter = new ArrayWheelAdapter<String>(
				DateSelectActivity.this, smallDays);
		normalDaysAdapter = new ArrayWheelAdapter<String>(
				DateSelectActivity.this, normalDays);
		bigDaysAdapter = new ArrayWheelAdapter<String>(DateSelectActivity.this,
				bigDays);
		mViewYear.setViewAdapter(yearsAdapter);
		mViewYear.setCurrentItem(getTodayYear());
		mViewYear.setCyclic(true);
		mViewMonth.setViewAdapter(monthsAdapter);
		mViewMonth.setCurrentItem(getTodayMonth());
		mViewMonth.setCyclic(true);
		if (isBigMonth(getTodayMonth() + 1)) {
			mViewDay.setViewAdapter(bigDaysAdapter);
		} else if (getTodayMonth() == 1
				&& isLeapYear(years[mViewYear.getCurrentItem()].toString()
						.trim())) {
			mViewDay.setViewAdapter(smallDaysAdapter);
		} else if (getTodayMonth() == 1) {
			mViewDay.setViewAdapter(tinyDaysAdapter);
		} else {
			mViewDay.setViewAdapter(normalDaysAdapter);
		}
		mViewDay.setCurrentItem(getTodayDay());
		mViewDay.setCyclic(true);
	}

	/**
	 * 获取当前日期的天数的日子
	 * 
	 * @return
	 */
	private int getTodayDay() {
		int position = 0;
		String today = getToday();
		String day = today.substring(8, 10);
		day = day + " 日";
		for (int i = 0; i < bigDays.length; i++) {
			if (day.equals(bigDays[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 获取当前日期的月数的位置
	 * 
	 * @return
	 */
	private int getTodayMonth() {
		// 2015年12月01日
		int position = 0;
		String today = getToday();
		String month = today.substring(5, 7);
		month = month + " 月";
		for (int i = 0; i < months.length; i++) {
			if (month.equals(months[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 获取当天的年份
	 * 
	 * @return
	 */
	private int getTodayYear() {
		int position = 0;
		String today = getToday();
		String year = today.substring(0, 4);
		year = year + " 年";
		for (int i = 0; i < years.length; i++) {
			if (year.equals(years[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 设置当前显示的年份
	 * 
	 * @param year
	 */
	public void setCurrentYear(String year) {
		boolean overYear = true;
		year = year + " 年";
		for (int i = 0; i < years.length; i++) {
			if (year.equals(years[i])) {
				mViewYear.setCurrentItem(i);
				overYear = false;
				break;
			}
		}
		if (overYear) {
			Log.e("", "设置的年份超出了数组的范围");
		}
	}

	/**
	 * 设置当前显示的月份
	 * 
	 * @param month
	 */
	public void setCurrentMonth(String month) {
		month = month + " 月";
		for (int i = 0; i < months.length; i++) {
			if (month.equals(months[i])) {
				mViewMonth.setCurrentItem(i);
				break;
			}
		}
	}

	/**
	 * 设置当前显示的日期号
	 * 
	 * @param day
	 *            14
	 */
	public void setCurrentDay(String day) {
		day = day + " 日";
		for (int i = 0; i < smallDays.length; i++) {
			if (day.equals(smallDays[i])) {
				mViewDay.setCurrentItem(i);
				break;
			}
		}
	}

	/**
	 * 判断是否是闰年
	 * 
	 * @param year
	 * @return
	 */
	private boolean isLeapYear(String year) {
		int temp = Integer.parseInt(year);
		return temp % 4 == 0 && (temp % 100 != 0 || temp % 400 == 0) ? true
				: false;
	}

	/**
	 * 获取今天的日期
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private String getToday() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	private String getTodayCommonPattern() {
		String str = TimeUtils.TimeStamp2Date(
				String.valueOf(System.currentTimeMillis()),
				TimeUtils.FORMAT_PATTERN_DATE);
		return str;
	}

	/**
	 * 判断是否是大月
	 * 
	 * @param month
	 * @return
	 */
	private boolean isBigMonth(int month) {
		boolean isBigMonth = false;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			isBigMonth = true;
			break;

		default:
			isBigMonth = false;
			break;
		}
		return isBigMonth;
	}

	/**
	 * 获取选择的日期的值
	 * 
	 * @return
	 */
	public String getSelectedDate() {
		return years[mViewYear.getCurrentItem()].toString().trim() + "-"
				+ months[mViewMonth.getCurrentItem()].toString().trim() + "-"
				+ bigDays[mViewDay.getCurrentItem()].toString().trim();
	}

	// 2015-08-11 12:10:00
	public String getSelectedDateValue() {
		String year = years[mViewYear.getCurrentItem()].toString().trim();
		int indexYear = year.indexOf("年");
		year = year.substring(0, indexYear).trim();

		String month = months[mViewMonth.getCurrentItem()].toString().trim();
		int indexMonth = month.indexOf("月");
		month = month.substring(0, indexMonth).trim();

		String day = bigDays[mViewDay.getCurrentItem()].toString().trim();
		int indexDay = day.indexOf("日");
		day = day.substring(0, indexDay).trim();

		return year + "-" + month + "-" + day;

	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		String trim = null;
		switch (wheel.getId()) {
		case R.id.id_year:
			trim = DateUtils.splitDateString(
					years[mViewYear.getCurrentItem()].toString()).trim();
			if (isLeapYear(trim)) {
				if (currentMonth == 2) {
					mViewDay.setViewAdapter(smallDaysAdapter);
				} else if (isBigMonth(currentMonth)) {
					mViewDay.setViewAdapter(bigDaysAdapter);
				} else {
					mViewDay.setViewAdapter(normalDaysAdapter);
				}
			} else if (currentMonth == 2) {
				mViewDay.setViewAdapter(tinyDaysAdapter);
			} else if (isBigMonth(currentMonth)) {
				mViewDay.setViewAdapter(bigDaysAdapter);
			} else {
				mViewDay.setViewAdapter(smallDaysAdapter);
			}
			break;
		case R.id.id_month:
			trim = DateUtils.splitDateString(
					months[mViewMonth.getCurrentItem()].toString()).trim();
			currentMonth = Integer.parseInt(trim);
			switch (currentMonth) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				mViewDay.setViewAdapter(bigDaysAdapter);
				break;
			case 2:
				String yearString = DateUtils.splitDateString(
						years[mViewYear.getCurrentItem()].toString()).trim();
				if (isLeapYear(yearString)) {
					mViewDay.setViewAdapter(smallDaysAdapter);
				} else {
					mViewDay.setViewAdapter(tinyDaysAdapter);
				}
				break;
			default:
				mViewDay.setViewAdapter(smallDaysAdapter);
				break;
			}
			break;
		case R.id.id_day:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_select_cance_tv: {
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
		case R.id.date_select_confirm_tv: {
			String today = getTodayCommonPattern();
			String dateYMD = today.substring(0, 10);
			String dateHMM = "00:00:00";

			// String year = today.substring(0, 4);
			// String month = today.substring(5, 7);
			// String day = today.substring(8, 10);
			//
			// String selectDate = getSelectedDateValue();
			// String selectYear = selectDate.substring(0, 4);
			// String selectMonth = selectDate.substring(5, 7);
			// String selectDay = selectDate.substring(8, 10);

			Log.e("xxx_Date1", dateYMD + " " + dateHMM);
			Log.e("xxx_Date2", getSelectedDateValue() + " " + dateHMM);
			long nowDate = TimeUtils.dateToLong(dateYMD + " " + dateHMM,
					TimeUtils.FORMAT_PATTERN_DATE);
			long selectDate = TimeUtils.dateToLong(getSelectedDateValue() + " "
					+ dateHMM, TimeUtils.FORMAT_PATTERN_DATE);

			if (selectDate >= nowDate) {
				if (nowDate == selectDate) {
					PersonInfoActivity.sIsNowDate = true;
				} else {
					PersonInfoActivity.sIsNowDate = false;
				}
				
				
				Intent intent = new Intent();
				intent.putExtra("date", getSelectedDate());
				intent.putExtra("date_value", getSelectedDateValue());
				setResult(RESULT_OK, intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.date_after), Toast.LENGTH_SHORT)
						.show();
			}

			break;
		}
		default:
			break;
		}
	}
}
