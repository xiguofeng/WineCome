package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.entity.OrderState;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.logic.OrderLogic;
import com.xgf.winecome.qrcode.google.zxing.client.CaptureActivity;
import com.xgf.winecome.ui.view.CircleTimerView;
import com.xgf.winecome.ui.view.CircleTimerView.CircleTimerListener;
import com.xgf.winecome.ui.view.widget.AlertDialog;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.ActivitiyInfoManager;
import com.xgf.winecome.utils.OrderManager;
import com.xgf.winecome.utils.TimeUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderStateActivity extends Activity implements OnClickListener, CircleTimerListener {

	public static final String ORIGIN_FROM_PAY_ACTION = "pay";

	public static final String ORIGIN_FROM_QR_RESULT_ACTION = "qr_result";

	public static final int TIME_UPDATE = 1;

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

	private String mNowAction = ORIGIN_FROM_PAY_ACTION;

	private CircleTimerView mTimerView;

	private int mStateCode = 0;

	private String mDeliveryTime;

	private int mTiming = 0;

	protected CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_CANCEL_SUC: {
				Toast.makeText(mContext, getString(R.string.order_cancel_suc), Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case OrderLogic.ORDER_CANCEL_FAIL: {
				Toast.makeText(mContext, getString(R.string.order_cancel_fail), Toast.LENGTH_SHORT).show();
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
			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.show();
			}
		}

	};

	private Handler mTimeHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIME_UPDATE: {
				long deliveryTime = TimeUtils.dateToLong(mDeliveryTime, TimeUtils.FORMAT_PATTERN_DATE);
				int waitTime = (int) (deliveryTime - (System.currentTimeMillis() / 1000));
				if (waitTime > 0) {
					mTimingTv.setText(TimeUtils.secToTime(waitTime));
					mTimeHandler.sendEmptyMessageDelayed(TIME_UPDATE, 1000);
				}
				break;
			}
			default:
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_state);
		mContext = OrderStateActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		if (!ActivitiyInfoManager.activitityMap.containsKey(ActivitiyInfoManager.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap.put(ActivitiyInfoManager.getCurrentActivityName(mContext), this);
		}
		// AppManager.getInstance().addActivity(OrderStateActivity.this);
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

		// mTimerView = (CircleTimerView)
		// findViewById(R.id.custom_timer_dialog_loading);
		// mTimerView.setCircleTimerListener(this);
	}

	private void setUpListener() {
		mQrcodeIv.setOnClickListener(this);
		mBackIv.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	private void setUpData() {
		String state = getIntent().getStringExtra("order_state");
		mDeliveryTime = getIntent().getStringExtra("delivery_time");
		if (TextUtils.isEmpty(mDeliveryTime)) {
			mDeliveryTime = TimeUtils.TimeStamp2Date(String.valueOf(System.currentTimeMillis() + 20 * 60 * 1000),
					TimeUtils.FORMAT_PATTERN_DATE);
		}
		mStateCode = Integer.parseInt(state);

		mTimeHandler.sendEmptyMessage(TIME_UPDATE);
		// mStateCode = 3;
		switch (mStateCode) {
		case 0: {

			break;
		}
			// 配送
		case 3: {
			mStepTwoTv.setTextColor(Color.BLACK);
			mStepTwoTv.setText(getString(R.string.began_delivery));
			mStepTwoIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));
			mWaitViewRl.setVisibility(View.VISIBLE);
			mTimingTv.setText("00:00:00");
			break;
		}

			// 收货
		case 4: {
			mStepTwoTv.setTextColor(Color.BLACK);
			mStepTwoTv.setText(getString(R.string.began_delivery));
			mStepTwoIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));

			mStepThreeTv.setTextColor(Color.BLACK);
			mStepThreeIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));
			break;

		}

			// 验货
		case 5: {

			mStepTwoTv.setTextColor(Color.BLACK);
			mStepTwoTv.setText(getString(R.string.began_delivery));
			mStepTwoIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));

			mStepThreeTv.setTextColor(Color.BLACK);
			mStepThreeTv.setText(getString(R.string.confirm_goods));
			mStepThreeIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));

			mStepFourTv.setTextColor(Color.BLACK);
			mStepFourTv.setText(getString(R.string.identification_goods));
			mStepFourIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));
			break;
		}

			// 已取消
		case 6: {

			mStepTwoTv.setTextColor(Color.BLACK);
			mStepTwoTv.setText(getString(R.string.began_delivery));
			mStepTwoIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));

			mStepThreeTv.setTextColor(Color.BLACK);
			mStepThreeTv.setText(getString(R.string.confirm_goods));
			mStepThreeIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));

			mStepFourTv.setTextColor(Color.BLACK);
			mStepFourTv.setText(getString(R.string.identification_goods));
			mStepFourIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));
			break;
		}

			// 已删除
		case 7: {

			mStepTwoTv.setTextColor(Color.BLACK);
			mStepTwoTv.setText(getString(R.string.began_delivery));
			mStepTwoIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));

			mStepThreeTv.setTextColor(Color.BLACK);
			mStepThreeTv.setText(getString(R.string.confirm_goods));
			mStepThreeIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));

			mStepFourTv.setTextColor(Color.BLACK);
			mStepFourTv.setText(getString(R.string.identification_goods));
			mStepFourIv.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));
			break;
		}

		default:
			break;
		}

		mCancelBtn.setVisibility(View.GONE);
		String orderStateCodeStr = String.valueOf(mStateCode);
		if (orderStateCodeStr.equals(OrderState.ORDER_STATUS_ORDERED)
				|| orderStateCodeStr.equals(OrderState.ORDER_STATUS_GRABBED)) {
			mCancelBtn.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_state_qr_iv: {
			Intent intent = new Intent(OrderStateActivity.this, CaptureActivity.class);
			intent.setAction(CaptureActivity.ORIGIN_FROM_ORDER_STATE_ACTION);
			startActivity(intent);
			break;
		}
		case R.id.order_state_back_iv: {
			finish();
			break;
		}

		case R.id.order_state_cancel_btn: {
			String orderStateCodeStr = String.valueOf(mStateCode);
			if (orderStateCodeStr.equals(OrderState.ORDER_STATUS_ORDERED)
					|| orderStateCodeStr.equals(OrderState.ORDER_STATUS_GRABBED)) {
				new AlertDialog(OrderStateActivity.this).builder().setTitle(getString(R.string.prompt))
						.setMsg(getString(R.string.order_cancel_confirm))
						.setPositiveButton(getString(R.string.confirm), new OnClickListener() {
							@Override
							public void onClick(View v) {

								if (null != mCustomProgressDialog) {
									mCustomProgressDialog.show();
								}
								OrderLogic.cancelOrder(mContext, mHandler, OrderManager.getsCurrentOrderId());
							}
						}).setNegativeButton(getString(R.string.cancal), new OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						}).show();

			} else {
				Toast.makeText(mContext, getString(R.string.order_cancel_no), Toast.LENGTH_SHORT).show();
			}
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onTimerStop() {

	}

	@Override
	public void onTimerStart(int time) {

	}

	@Override
	public void onTimerPause(int time) {

	}

}
