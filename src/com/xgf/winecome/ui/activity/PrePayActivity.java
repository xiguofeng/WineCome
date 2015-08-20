package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
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
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.logic.OrderLogic;
import com.xgf.winecome.pay.alipay.AlipayApi;
import com.xgf.winecome.pay.alipay.PayResult;
import com.xgf.winecome.ui.view.CustomProgressDialog;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.ActivitiyInfoManager;
import com.xgf.winecome.utils.OrderManager;

public class PrePayActivity extends Activity implements OnClickListener {

	private Context mContext;

	private RelativeLayout mAlipayRl;
	private RelativeLayout mWeChatRl;
	private RelativeLayout mUnionpayRl;

	private CheckBox mAlipayCb;
	private CheckBox mWeChatCb;
	private CheckBox mUnionpayCb;

	private ImageView mBackIv;

	private Button mPayConfirmBtn;

	private HashMap<String, Object> mMsgMap = new HashMap<String, Object>();

	private CustomProgressDialog2 mCustomProgressDialog;

	private String mCurrentPayWay;

	private String mCurrentSelectPayWay;

	private String mPrice;

	private boolean isPaySuc = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_PRE_PAY_TYPE_SET_SUC: {
				if (null != msg.obj) {
					mMsgMap.clear();
					mMsgMap.putAll((Map<? extends String, ? extends Object>) msg.obj);
					OrderManager.setsCurrentOrder(((ArrayList<Order>) mMsgMap
							.get(MsgResult.ORDER_TAG)).get(0));
					mCurrentSelectPayWay = mCurrentPayWay;

					AlipayApi apAlipayApi = new AlipayApi();
					apAlipayApi.pay(PrePayActivity.this, mAlipayHandler);
				}
				break;
			}
			case OrderLogic.ORDER_PRE_PAY_TYPE_SET_FAIL: {
				Toast.makeText(mContext, R.string.pre_pay_set_way_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case OrderLogic.ORDER_PRE_PAY_TYPE_SET_EXCEPTION: {
				break;
			}
			case OrderLogic.ORDER_PAY_RESULT_CHECK_SUC: {
				setResult(true);
				if (null != msg.obj) {
				}
				break;
			}
			case OrderLogic.ORDER_PAY_RESULT_CHECK_FAIL: {
				setResult(true);
				// Toast.makeText(mContext, R.string.login_fail,
				// Toast.LENGTH_SHORT).show();
				break;
			}
			case OrderLogic.ORDER_PAY_RESULT_CHECK_EXCEPTION: {
				setResult(true);
				break;
			}
			case OrderLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
			if (null != mCustomProgressDialog
					&& mCustomProgressDialog.isShowing()) {
				mCustomProgressDialog.dismiss();
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

					mCustomProgressDialog.show();
					OrderLogic.payResultCheck(mContext, mHandler,
							OrderManager.getsCurrentOrderId(), "true");

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
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pre_pay);
		mContext = PrePayActivity.this;
		if (!ActivitiyInfoManager.activitityMap
				.containsKey(ActivitiyInfoManager
						.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap
					.put(ActivitiyInfoManager.getCurrentActivityName(mContext),
							this);
		}
		// AppManager.getInstance().addActivity(PrePayActivity.this);
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mAlipayRl = (RelativeLayout) findViewById(R.id.pre_pay_alipay_rl);
		mWeChatRl = (RelativeLayout) findViewById(R.id.pre_pay_wechat_rl);
		mUnionpayRl = (RelativeLayout) findViewById(R.id.pre_pay_unionpay_rl);

		mAlipayCb = (CheckBox) findViewById(R.id.pre_pay_alipay_cb);
		mWeChatCb = (CheckBox) findViewById(R.id.pre_pay_wechat_cb);
		mUnionpayCb = (CheckBox) findViewById(R.id.pre_pay_unionpay_cb);

		mBackIv = (ImageView) findViewById(R.id.pre_pay_back_iv);
		mPayConfirmBtn = (Button) findViewById(R.id.pre_pay_confirm_btn);
	}

	private void setUpListener() {
		mBackIv.setOnClickListener(this);
		mPayConfirmBtn.setOnClickListener(this);

		mAlipayCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
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
					mWeChatCb.setChecked(false);
					mAlipayCb.setChecked(false);
					mCurrentSelectPayWay = Constants.PAY_WAY_UNIONPAY;
				}
			}
		});

	}

	private void setUpData() {
		mPrice = getIntent().getStringExtra("order_pre_price");
	}

	private void setResult(boolean isSuc) {
		Intent intent = new Intent();
		intent.putExtra("pay_result", isSuc);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.pre_pay_confirm_btn: {
			mCustomProgressDialog.show();
			OrderLogic.setPrePayWay(mContext, mHandler,
					OrderManager.getsCurrentOrderId(), mPrice,
					mCurrentSelectPayWay);
			break;
		}

		case R.id.pre_pay_back_iv: {
			PrePayActivity.this.finish();
			break;
		}
		default:
			break;
		}
	}

}
