package com.xgf.winecome.ui.activity;

import com.xgf.winecome.R;
import com.xgf.winecome.network.logic.IntegralGoodsLogic;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntegralQueryResultActivity extends Activity implements OnClickListener {

	private LinearLayout mGoOnMallLl;

	private LinearLayout mOrderQueryLl;

	private ImageView mBackIv;

	private TextView mTotalTv;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case IntegralGoodsLogic.INTEGRAL_TOTAL_GET_SUC: {
				if (null != msg.obj) {

				}
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_TOTAL_GET_FAIL: {
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_TOTAL_GET_EXCEPTION: {
				break;
			}

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_result);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mBackIv = (ImageView) findViewById(R.id.integral_result_back_iv);
		mTotalTv = (TextView) findViewById(R.id.integral_result_result_tv);

	}

	private void setUpListener() {
		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.integral_result_back_iv: {
			finish();
			break;
		}

		default:
			break;
		}
	}

}
