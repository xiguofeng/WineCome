package com.xgf.winecome.ui.activity;

import com.xgf.winecome.R;
import com.xgf.winecome.network.logic.UserLogic;
import com.xgf.winecome.utils.UserInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderQueryActivity extends Activity implements OnClickListener,
		TextWatcher {
	public static final int TIME_UPDATE = 1;

	private LinearLayout mQueryLl;
	private LinearLayout mAuthCodeLl;
	private LinearLayout mReplaceLl;

	private RelativeLayout mAuthRl;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private TextView mTimingTv;
	private TextView mPhoneTv;

	private Context mContext;

	private ImageView mBackIv;

	private String mPhone;
	private String mAuthCode;

	private boolean mIsNeedAuth = false;

	private int mTiming = 60;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.SEND_AUTHCODE_SUC: {
				if (null != msg.obj) {
					mAuthCode = (String) msg.obj;
					UserInfoManager.setPhone(mContext, mPhone);
					UserInfoManager.setIsMustAuth(mContext, false);
				}
				mTimeHandler.sendEmptyMessage(TIME_UPDATE);
				break;
			}
			case UserLogic.SEND_AUTHCODE_FAIL: {
				Toast.makeText(mContext, R.string.auth_get_fail,
						Toast.LENGTH_SHORT).show();
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
					mAuthCodeLl.setBackgroundColor(getResources().getColor(
							R.color.gray_divide_line));
					mTimeHandler.sendEmptyMessageDelayed(TIME_UPDATE, 1000);
				} else {
					mAuthCodeLl.setClickable(true);
					mAuthCodeLl.setBackgroundColor(getResources().getColor(
							R.color.orange_bg));
					mTimingTv
							.setText(getString(R.string.get_verification_code));
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
		setContentView(R.layout.order_query);
		mContext = OrderQueryActivity.this;
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mQueryLl = (LinearLayout) findViewById(R.id.order_query_submit_ll);
		mAuthCodeLl = (LinearLayout) findViewById(R.id.order_query_ver_code_ll);
		mReplaceLl = (LinearLayout) findViewById(R.id.order_query_replace_phone_ll);

		mPhoneEt = (EditText) findViewById(R.id.order_query_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.order_query_ver_code_et);
		mTimingTv = (TextView) findViewById(R.id.order_query_ver_code_btn_tv);
		mPhoneTv = (TextView) findViewById(R.id.order_query_phone_tv);

		mAuthRl = (RelativeLayout) findViewById(R.id.order_query_ver_code_rl);

		mBackIv = (ImageView) findViewById(R.id.order_query_back_iv);
	}

	private void setUpListener() {
		mQueryLl.setOnClickListener(this);
		mAuthCodeLl.setOnClickListener(this);
		mReplaceLl.setOnClickListener(this);

		mPhoneEt.addTextChangedListener(this);
		mVerCodeEt.addTextChangedListener(this);

		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
		if (!UserInfoManager.getIsMustAuth(mContext)
				&& !TextUtils.isEmpty(UserInfoManager.getPhone(mContext))) {
			mPhone = UserInfoManager.getPhone(mContext);
			mAuthRl.setVisibility(View.GONE);
			mPhoneTv.setVisibility(View.VISIBLE);
			mPhoneTv.setText(mPhone);
			mPhoneEt.setVisibility(View.GONE);
			mReplaceLl.setVisibility(View.VISIBLE);
		} else {
			mIsNeedAuth = true;
			mAuthRl.setVisibility(View.VISIBLE);
			mPhoneTv.setVisibility(View.GONE);
			mPhoneEt.setVisibility(View.VISIBLE);
			mReplaceLl.setVisibility(View.GONE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_query_submit_ll: {
			if (!mIsNeedAuth) {
				if (!TextUtils.isEmpty(mPhone)) {
					Intent intent = new Intent(OrderQueryActivity.this,
							OrderListActivity.class);
					intent.putExtra("phone", mPhone);
					startActivity(intent);
					OrderQueryActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				} else {
					mIsNeedAuth = true;
					mAuthRl.setVisibility(View.VISIBLE);
					mPhoneTv.setVisibility(View.GONE);
					mPhoneEt.setVisibility(View.VISIBLE);
					mReplaceLl.setVisibility(View.GONE);
				}
			} else if (!TextUtils.isEmpty(mPhone)
					&& !TextUtils.isEmpty(mAuthCode)
					&& mAuthCode.equals(mVerCodeEt.getText().toString().trim())
					&& mVerCodeEt.getText().toString().trim()
							.endsWith(mAuthCode)) {
				Intent intent = new Intent(OrderQueryActivity.this,
						OrderListActivity.class);
				intent.putExtra("phone", mPhone);
				startActivity(intent);
				OrderQueryActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				Toast.makeText(mContext,
						getString(R.string.mobile_phone_and_code_hint),
						Toast.LENGTH_SHORT).show();
			}
			// User user = new User();
			// UserLogic.login(mContext, mLoginHandler, user);

			break;
		}
		case R.id.order_query_ver_code_ll: {
			mPhone = mPhoneEt.getText().toString().trim();
			if (!TextUtils.isEmpty(mPhone)) {
				UserLogic.sendAuthCode(mContext, mHandler, mPhone);

			} else {
				Toast.makeText(mContext, getString(R.string.mobile_phone_hint),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.order_query_back_iv: {
			finish();
			break;
		}

		case R.id.order_query_replace_phone_ll: {
			mIsNeedAuth = true;
			mAuthRl.setVisibility(View.VISIBLE);
			mPhoneTv.setVisibility(View.GONE);
			mPhoneEt.setVisibility(View.VISIBLE);
			mReplaceLl.setVisibility(View.GONE);

			UserInfoManager.setPhone(mContext, "");
			UserInfoManager.setIsMustAuth(mContext, true);
			break;
		}

		default:
			break;
		}
	}

}
