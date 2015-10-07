package com.xgf.winecome.ui.activity;

import com.xgf.winecome.R;
import com.xgf.winecome.network.logic.IntegralGoodsLogic;
import com.xgf.winecome.network.logic.UserLogic;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.UserInfoManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IntegralInfoInput extends Activity implements OnClickListener {
	public static final int TIME_UPDATE = 1;

	private LinearLayout mQueryLl;
	private LinearLayout mAuthCodeLl;
	private LinearLayout mReplaceLl;

	private RelativeLayout mAuthRl;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private EditText mAddressEt;
	private EditText mPersonNameEt;
	private TextView mTimingTv;
	private TextView mPhoneTv;

	private Context mContext;

	private ImageView mBackIv;

	private String mPhone;
	private String mAuthCode;
	private String mAddress;
	private String mPersonName;

	private String mId;

	protected CustomProgressDialog2 mCustomProgressDialog;
	private int mTiming = 60;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case IntegralGoodsLogic.INTEGRAL_GOODS_EXCHANGE_SUC: {
				Toast.makeText(mContext, "兑换成功", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_GOODS_EXCHANGE_FAIL: {
				Toast.makeText(mContext, "兑换失败", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_GOODS_EXCHANGE_EXCEPTION: {
				Toast.makeText(mContext, "兑换失败", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case UserLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}

			if (null != mCustomProgressDialog && mCustomProgressDialog.isShowing()) {
				mCustomProgressDialog.dismiss();
			}

		}

	};

	Handler mAuthHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.SEND_AUTHCODE_SUC: {
				if (null != msg.obj) {
					mAuthCode = (String) msg.obj;
					UserInfoManager.setPhone(mContext, mPhone);
					UserInfoManager.setIsMustAuth(mContext, false);
					mReplaceLl.setVisibility(View.GONE);
				}
				mTimeHandler.sendEmptyMessage(TIME_UPDATE);
				break;
			}
			case UserLogic.SEND_AUTHCODE_FAIL: {
				Toast.makeText(mContext, R.string.auth_get_fail, Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.SEND_AUTHCODE_EXCEPTION: {
				break;
			}
			case UserLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}

			if (null != mCustomProgressDialog && mCustomProgressDialog.isShowing()) {
				mCustomProgressDialog.dismiss();
			}

		}

	};

	private Handler mTimeHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIME_UPDATE: {
				if (mTiming > 0) {
					mTiming--;
					mTimingTv.setText(String.valueOf(mTiming) + "秒");
					mAuthCodeLl.setClickable(false);
					mAuthCodeLl.setBackgroundColor(getResources().getColor(R.color.gray_divide_line));
					mTimeHandler.sendEmptyMessageDelayed(TIME_UPDATE, 1000);
				} else {
					mAuthCodeLl.setClickable(true);
					mAuthCodeLl.setBackgroundColor(getResources().getColor(R.color.orange_bg));
					mTimingTv.setText(getString(R.string.get_verification_code));
					mTiming = 60;
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
		setContentView(R.layout.integral_info_input);
		mContext = IntegralInfoInput.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mQueryLl = (LinearLayout) findViewById(R.id.integral_info_input_submit_ll);
		mAuthCodeLl = (LinearLayout) findViewById(R.id.integral_info_input_ver_code_ll);
		mReplaceLl = (LinearLayout) findViewById(R.id.integral_info_input_replace_phone_ll);

		mPhoneEt = (EditText) findViewById(R.id.integral_info_input_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.integral_info_input_ver_code_et);
		mAddressEt = (EditText) findViewById(R.id.integral_info_input_address_et);
		mPersonNameEt = (EditText) findViewById(R.id.integral_info_input_name_et);

		mTimingTv = (TextView) findViewById(R.id.integral_info_input_ver_code_btn_tv);
		mPhoneTv = (TextView) findViewById(R.id.integral_info_input_phone_tv);

		mAuthRl = (RelativeLayout) findViewById(R.id.integral_info_input_ver_code_rl);

		mBackIv = (ImageView) findViewById(R.id.integral_info_input_back_iv);
	}

	private void setUpListener() {
		mQueryLl.setOnClickListener(this);
		mAuthCodeLl.setOnClickListener(this);
		mReplaceLl.setOnClickListener(this);

		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
		mId = getIntent().getStringExtra("id");

		if (!UserInfoManager.getIsMustAuth(mContext) && !TextUtils.isEmpty(UserInfoManager.getPhone(mContext))) {
			mPhone = UserInfoManager.getPhone(mContext);
			mPhoneTv.setVisibility(View.VISIBLE);
			mPhoneTv.setText(mPhone);
			mPhoneEt.setVisibility(View.GONE);
			mReplaceLl.setVisibility(View.VISIBLE);
		} else {
			mAuthRl.setVisibility(View.VISIBLE);
			mPhoneTv.setVisibility(View.GONE);
			mPhoneEt.setVisibility(View.VISIBLE);
			mReplaceLl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.integral_info_input_submit_ll: {
			if (TextUtils.isEmpty(mPhone)) {
				mPhone = mPhoneEt.getText().toString().trim();
			}
			if (TextUtils.isEmpty(mPhone)) {
				Toast.makeText(mContext, getString(R.string.mobile_phone_and_code_hint), Toast.LENGTH_SHORT).show();
			}
			mAuthCode = mVerCodeEt.getText().toString().trim();
			mAddress = mAddressEt.getText().toString().trim();
			mPersonName = mPersonNameEt.getText().toString().trim();
			if (TextUtils.isEmpty(mAddress)) {
				Toast.makeText(mContext, getString(R.string.detail_info_hint), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (TextUtils.isEmpty(mPersonName)) {
				Toast.makeText(mContext, getString(R.string.person_name_hint), Toast.LENGTH_SHORT).show();
				return;
			}

			if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mAuthCode) && !TextUtils.isEmpty(mAddress)
					&& mAuthCode.equals(mVerCodeEt.getText().toString().trim())) {
				if (null != mCustomProgressDialog) {
					mCustomProgressDialog.show();
				}
				//TODO
				IntegralGoodsLogic.exchange(mContext, mHandler, mPhone, mId,mAddress);

			} else {
				Toast.makeText(mContext, getString(R.string.mobile_phone_and_code_hint), Toast.LENGTH_SHORT).show();
			}
			// User user = new User();
			// UserLogic.login(mContext, mLoginHandler, user);

			break;
		}
		case R.id.integral_info_input_ver_code_ll: {
			if (TextUtils.isEmpty(mPhone)) {
				mPhone = mPhoneEt.getText().toString().trim();
			}
			if (!TextUtils.isEmpty(mPhone)) {
				UserLogic.sendAuthCode(mContext, mAuthHandler, mPhone);
			} else {
				Toast.makeText(mContext, getString(R.string.mobile_phone_hint), Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.integral_info_input_back_iv: {
			finish();
			break;
		}

		case R.id.integral_info_input_replace_phone_ll: {
			mAuthRl.setVisibility(View.VISIBLE);
			mPhoneTv.setVisibility(View.GONE);
			mPhoneEt.setVisibility(View.VISIBLE);
			mReplaceLl.setVisibility(View.GONE);

			mPhone = "";
			// UserInfoManager.setPhone(mContext, "");
			// UserInfoManager.setIsMustAuth(mContext, true);
			break;
		}

		default:
			break;
		}
	}

}
