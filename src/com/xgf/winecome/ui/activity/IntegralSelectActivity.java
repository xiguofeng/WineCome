package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class IntegralSelectActivity extends Activity implements
		OnClickListener, OnWheelChangedListener {
	private TextView mCancelTv;
	private TextView mConfirmTv;

	private WheelView mViewIntegral;

	private ArrayWheelAdapter<String> integralAdapter;

	private String[] mIntegral = new String[]{"全部","0-5000","5000-10000","10000以上"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_select);
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

		mViewIntegral = (WheelView) findViewById(R.id.id_integral);

		mCancelTv = (TextView) findViewById(R.id.integral_select_cance_tv);
		mConfirmTv = (TextView) findViewById(R.id.integral_select_confirm_tv);
	}

	private void setUpListener() {
		// 添加change事件
		mViewIntegral.addChangingListener(this);
		// 添加change事件
		// 添加change事件
		// 添加onclick事件
		mConfirmTv.setOnClickListener(this);
		mCancelTv.setOnClickListener(this);
	}

	private void setUpData() {
		// 设置可见条目数量
		mViewIntegral.setVisibleItems(4);
		setData();
	}

	private void setData() {
		integralAdapter = new ArrayWheelAdapter<String>(
				IntegralSelectActivity.this, mIntegral);

		mViewIntegral.setViewAdapter(integralAdapter);
		mViewIntegral.setCurrentItem(0);
		mViewIntegral.setCyclic(true);

	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		String trim = null;
		switch (wheel.getId()) {
		case R.id.id_integral:
			trim = mIntegral[mViewIntegral.getCurrentItem()].toString().trim();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.integral_select_cance_tv: {
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
		case R.id.integral_select_confirm_tv: {
			Intent intent = new Intent();
			intent.putExtra("integral", mIntegral[mViewIntegral.getCurrentItem()].toString().trim());
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		default:
			break;
		}
	}

}
