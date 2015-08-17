package com.xgf.winecome.ui.activity;

import com.xgf.winecome.AppManager;
import com.xgf.winecome.R;
import com.xgf.winecome.network.logic.OrderLogic;
import com.xgf.winecome.qrcode.google.zxing.client.CaptureActivity;
import com.xgf.winecome.utils.OrderManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderStateActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ImageView mBackIv;

	private ImageView mQrcodeIv;

	private RelativeLayout mWaitViewRl;

	private TextView mStepOneTv;

	private TextView mStepTwoTv;

	private TextView mStepThreeTv;

	private TextView mStepFourTv;

	private TextView mTimingTv;

	private ImageView mStepOneIv;

	private ImageView mStepTwoIv;

	private ImageView mStepThreeIv;

	private ImageView mStepFourIv;

	private Button mCancelBtn;

	private int mStateCode = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_CANCEL_SUC: {
				Toast.makeText(mContext, getString(R.string.order_cancel_suc),
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case OrderLogic.ORDER_CANCEL_FAIL: {
				Toast.makeText(mContext, getString(R.string.order_cancel_fail),
						Toast.LENGTH_SHORT).show();
				break;
			}
			case OrderLogic.ORDER_CANCEL_EXCEPTION: {
				break;
			}
			case OrderLogic.NET_ERROR: {
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
		setContentView(R.layout.order_state);
		mContext = OrderStateActivity.this;
		AppManager.getInstance().addActivity(OrderStateActivity.this);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mBackIv = (ImageView) findViewById(R.id.order_state_back_iv);
		mQrcodeIv = (ImageView) findViewById(R.id.order_state_qr_iv);

		mTimingTv = (TextView) findViewById(R.id.order_state_timing_tv);

		mStepOneTv = (TextView) findViewById(R.id.order_state_step_one_tv);
		mStepTwoTv = (TextView) findViewById(R.id.order_state_step_two_tv);
		mStepThreeTv = (TextView) findViewById(R.id.order_state_step_three_tv);
		mStepFourTv = (TextView) findViewById(R.id.order_state_step_four_tv);

		mStepOneIv = (ImageView) findViewById(R.id.order_state_step_one_iv);
		mStepTwoIv = (ImageView) findViewById(R.id.order_state_step_two_iv);
		mStepThreeIv = (ImageView) findViewById(R.id.order_state_step_three_iv);
		mStepFourIv = (ImageView) findViewById(R.id.order_state_step_four_iv);

		mCancelBtn = (Button) findViewById(R.id.order_state_cancel_btn);

		mWaitViewRl = (RelativeLayout) findViewById(R.id.order_state_wait_rl);
	}

	private void setUpListener() {
		mQrcodeIv.setOnClickListener(this);
		mBackIv.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	private void setUpData() {
		String state = getIntent().getStringExtra("order_state");

		mStateCode = Integer.parseInt(state);
		switch (mStateCode) {
		case 0: {

			break;
		}
		// 配送
		case 3: {
			mStepTwoTv.setTextColor(R.color.black_character);
			mStepTwoIv.setImageDrawable(getResources().getDrawable(
					R.drawable.dot_green));
			mWaitViewRl.setVisibility(View.VISIBLE);
			mTimingTv.setText("00:29:29");
		}

		// 收货
		case 4: {
			mStepTwoTv.setTextColor(R.color.black_character);
			mStepTwoIv.setImageDrawable(getResources().getDrawable(
					R.drawable.dot_green));

			mStepThreeTv.setTextColor(R.color.black_character);
			mStepThreeIv.setImageDrawable(getResources().getDrawable(
					R.drawable.dot_green));

		}

		// 验货
		case 5: {

			mStepTwoTv.setTextColor(R.color.black_character);
			mStepTwoIv.setImageDrawable(getResources().getDrawable(
					R.drawable.dot_green));

			mStepThreeTv.setTextColor(R.color.black_character);
			mStepThreeIv.setImageDrawable(getResources().getDrawable(
					R.drawable.dot_green));

			mStepFourTv.setTextColor(R.color.black_character);
			mStepFourIv.setImageDrawable(getResources().getDrawable(
					R.drawable.dot_green));
		}

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_state_qr_iv: {
			Intent intent = new Intent(OrderStateActivity.this,
					CaptureActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.order_state_back_iv: {
			finish();
			break;
		}

		case R.id.order_state_cancel_btn: {
			if (mStateCode < 3) {
				OrderLogic.cancelOrder(mContext, mHandler,
						OrderManager.getsCurrentOrderId());
			} else {
				Toast.makeText(mContext, getString(R.string.order_cancel_no),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		default:
			break;
		}
	}

}
