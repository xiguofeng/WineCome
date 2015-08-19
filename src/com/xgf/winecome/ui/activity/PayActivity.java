package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.config.Constants;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.logic.OrderLogic;
import com.xgf.winecome.pay.alipay.AlipayApi;
import com.xgf.winecome.pay.alipay.PayResult;
import com.xgf.winecome.ui.view.CustomProgressDialog;
import com.xgf.winecome.utils.ActivitiyInfoManager;
import com.xgf.winecome.utils.OrderManager;

public class PayActivity extends Activity implements OnClickListener {
	
	/**************************************************
	 *  支付状态 : 1-未支付
	 *            2-已支付
	 *            3-申请退款
	 *            4-已经退款
	 *            5-预支付
	 *************************************************/
	public static final String PAY_STATUS_UNPAID = "1";
	public static final String PAY_STATUS_PAID = "2";
	public static final String PAY_STATUS_APPLY_REFUND = "3";
	public static final String PAY_STATUS_REFUND = "4";
	public static final String PAY_STATUS_PREPAID = "5";

	private Context mContext;

	private RelativeLayout mCashRl;
	private RelativeLayout mPosRl;
	private RelativeLayout mAlipayRl;
	private RelativeLayout mWeChatRl;
	private RelativeLayout mUnionpayRl;

	private CheckBox mCashCb;
	private CheckBox mPosCb;
	private CheckBox mAlipayCb;
	private CheckBox mWeChatCb;
	private CheckBox mUnionpayCb;

	private Button mPayConfirmBtn;

	private ImageView mBackIv;

	private HashMap<String, Object> mMsgMap = new HashMap<String, Object>();

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private int mTotalNum = 0;

	private String mCurrentPayWay;

	private String mCurrentSelectPayWay;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_PAY_TYPE_SET_SUC: {
				if (null != msg.obj) {
					mMsgMap.clear();
					mMsgMap.putAll((Map<? extends String, ? extends Object>) msg.obj);
					OrderManager.setsCurrentOrder(((ArrayList<Order>) mMsgMap
							.get(MsgResult.ORDER_TAG)).get(0));
					mCurrentPayWay = mCurrentSelectPayWay;
					hanlder();
				}
				break;
			}
			case OrderLogic.ORDER_PAY_TYPE_SET_FAIL: {
				// Toast.makeText(mContext, R.string.login_fail,
				// Toast.LENGTH_SHORT).show();
				break;
			}
			case OrderLogic.ORDER_PAY_TYPE_SET_EXCEPTION: {
				break;
			}
			case OrderLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	};

	private Handler mAlipayHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case com.xgf.winecome.pay.alipay.Constants.SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(mContext,
							OrderStateActivity.class);
					intent.putExtra("order_state", "2");
					intent.putExtra("delivery_time", OrderManager
							.getsCurrentOrder().getDeliveryTime());
					startActivity(intent);
					PayActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				} else {

					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}
				}
				break;
			}
			case com.xgf.winecome.pay.alipay.Constants.SDK_CHECK_FLAG: {
				Toast.makeText(mContext, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			default:
				break;

			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		mContext = PayActivity.this;
		// AppManager.getInstance().addActivity(PayActivity.this);
		if (!ActivitiyInfoManager.activitityMap
				.containsKey(ActivitiyInfoManager
						.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap
					.put(ActivitiyInfoManager.getCurrentActivityName(mContext),
							this);
		}
		mProgressDialog = new CustomProgressDialog(mContext);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mCashRl = (RelativeLayout) findViewById(R.id.pay_cash_rl);
		mPosRl = (RelativeLayout) findViewById(R.id.pay_pos_rl);
		mAlipayRl = (RelativeLayout) findViewById(R.id.pay_alipay_rl);
		mWeChatRl = (RelativeLayout) findViewById(R.id.pay_wechat_rl);
		mUnionpayRl = (RelativeLayout) findViewById(R.id.pay_unionpay_rl);

		mCashCb = (CheckBox) findViewById(R.id.pay_cash_cb);
		mPosCb = (CheckBox) findViewById(R.id.pay_pos_cb);
		mAlipayCb = (CheckBox) findViewById(R.id.pay_alipay_cb);
		mWeChatCb = (CheckBox) findViewById(R.id.pay_wechat_cb);
		mUnionpayCb = (CheckBox) findViewById(R.id.pay_unionpay_cb);

		mPayConfirmBtn = (Button) findViewById(R.id.pay_confirm_btn);
		mBackIv = (ImageView) findViewById(R.id.pay_back_iv);
	}

	private void setUpListener() {

		mPosRl.setOnClickListener(this);
		mAlipayRl.setOnClickListener(this);
		mWeChatRl.setOnClickListener(this);
		mCashRl.setOnClickListener(this);
		mUnionpayRl.setOnClickListener(this);
		mCashCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mPosCb.setChecked(false);
					mAlipayCb.setChecked(false);
					mWeChatCb.setChecked(false);
					mUnionpayCb.setChecked(false);
					mCurrentSelectPayWay = Constants.PAY_WAY_CASHPAY;
				}
			}
		});
		mPosCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mCashCb.setChecked(false);
					mAlipayCb.setChecked(false);
					mWeChatCb.setChecked(false);
					mUnionpayCb.setChecked(false);
					mCurrentSelectPayWay = Constants.PAY_WAY_POSPAY;
				}
			}
		});
		mAlipayCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mCashCb.setChecked(false);
					mPosCb.setChecked(false);
					mWeChatCb.setChecked(false);
					mUnionpayCb.setChecked(false);
					mCurrentSelectPayWay = Constants.PAY_WAY_ALIPAY;
				}
			}
		});
		mWeChatCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mCashCb.setChecked(false);
					mPosCb.setChecked(false);
					mAlipayCb.setChecked(false);
					mUnionpayCb.setChecked(false);
					mCurrentSelectPayWay = Constants.PAY_WAY_WXPAY;
				}
			}
		});
		mUnionpayCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mCashCb.setChecked(false);
					mPosCb.setChecked(false);
					mWeChatCb.setChecked(false);
					mAlipayCb.setChecked(false);
					mCurrentSelectPayWay = Constants.PAY_WAY_UNIONPAY;
				}
			}
		});

		mPayConfirmBtn.setOnClickListener(this);
		mBackIv.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 80: {
				if (data.getBooleanExtra("pay_result", false)) {
					Toast.makeText(mContext, getString(R.string.pay_suc),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(mContext,
							OrderStateActivity.class);
					intent.putExtra("order_state", "2");
					intent.putExtra("delivery_time", OrderManager
							.getsCurrentOrder().getDeliveryTime());
					startActivity(intent);
					PayActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				} else {
					Toast.makeText(mContext, getString(R.string.pay_fail),
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void hanlder() {
		if (Constants.PAY_WAY_ALIPAY.equals(mCurrentPayWay)) {
			AlipayApi apAlipayApi = new AlipayApi();
			apAlipayApi.pay(PayActivity.this, mAlipayHandler);

		} else if (Constants.PAY_WAY_WXPAY.equals(mCurrentPayWay)) {
			AlipayApi apAlipayApi = new AlipayApi();
			apAlipayApi.pay(PayActivity.this, mAlipayHandler);

		} else if (Constants.PAY_WAY_UNIONPAY.equals(mCurrentPayWay)) {
			AlipayApi apAlipayApi = new AlipayApi();
			apAlipayApi.pay(PayActivity.this, mAlipayHandler);

		} else if (Constants.PAY_WAY_CASHPAY.equals(mCurrentPayWay)) {
			mGoodsList.clear();
			mGoodsList.addAll((Collection<? extends Goods>) mMsgMap
					.get(OrderManager.getsCurrentOrder().getId()));

			for (Goods goods : mGoodsList) {
				mTotalNum = Integer.parseInt(goods.getNum()) + mTotalNum;
			}

			if (mTotalNum >= 12) {
				Double preMoney = (Double.parseDouble(OrderManager
						.getsCurrentOrder().getAmount())) * 0.3;
				Intent intent = new Intent(mContext, PrePayActivity.class);
				intent.putExtra("order_pre_price", String.valueOf(preMoney));
				startActivityForResult(intent, 80);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				Intent intent = new Intent(mContext, OrderStateActivity.class);
				intent.putExtra("order_state", "2");
				intent.putExtra("delivery_time", OrderManager
						.getsCurrentOrder().getDeliveryTime());
				startActivity(intent);
				PayActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}

		} else if (Constants.PAY_WAY_POSPAY.equals(mCurrentPayWay)) {
			mGoodsList.clear();
			mGoodsList.addAll((Collection<? extends Goods>) mMsgMap
					.get(OrderManager.getsCurrentOrder().getId()));

			for (Goods goods : mGoodsList) {
				mTotalNum = Integer.parseInt(goods.getNum()) + mTotalNum;
			}

			if (mTotalNum >= 12) {
				Double preMoney = (Double.parseDouble(OrderManager
						.getsCurrentOrder().getAmount())) * 0.3;
				Intent intent = new Intent(mContext, PrePayActivity.class);
				intent.putExtra("order_pre_price", String.valueOf(preMoney));
				startActivityForResult(intent, 80);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				Intent intent = new Intent(mContext, OrderStateActivity.class);
				intent.putExtra("order_state", "2");
				intent.putExtra("delivery_time", OrderManager
						.getsCurrentOrder().getDeliveryTime());
				startActivity(intent);
				PayActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}

		}
	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_confirm_btn: {
			if (!TextUtils.isEmpty(mCurrentSelectPayWay)) {
				OrderLogic
						.setPayWay(mContext, mHandler,
								OrderManager.getsCurrentOrderId(),
								mCurrentSelectPayWay);
			} else {
				Toast.makeText(mContext, getString(R.string.pay_way_hint),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.pay_back_iv: {
			PayActivity.this.finish();
			break;
		}
		default:
			break;
		}
	}

}
