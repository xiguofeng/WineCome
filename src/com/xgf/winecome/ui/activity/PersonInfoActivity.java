package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.xgf.winecome.AppManager;
import com.xgf.winecome.R;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.network.logic.OrderLogic;
import com.xgf.winecome.network.logic.UserLogic;
import com.xgf.winecome.utils.CartManager;
import com.xgf.winecome.utils.LocationUtilsV5;
import com.xgf.winecome.utils.LocationUtilsV5.LocationCallback;
import com.xgf.winecome.utils.ActivitiyInfoManager;
import com.xgf.winecome.utils.OrderManager;
import com.xgf.winecome.utils.TimeUtils;
import com.xgf.winecome.utils.UserInfoManager;

public class PersonInfoActivity extends Activity implements OnClickListener,
		TextWatcher {
	public static final String ORIGIN_FROM_DETAIL_ACTION = "gooddetail";

	public static final String ORIGIN_FROM_MAIN_ACTION = "main";

	public static final String ORIGIN_FROM_CART_ACTION = "cart";

	public static final int TIME_UPDATE = 1;

	private Context mContext;

	private LinearLayout mAuthCodeLl;
	private LinearLayout mSubmitLl;
	private LinearLayout mInvoiceLl;
	private LinearLayout mBottomLl;
	private LinearLayout mInvoiceInfoLl;
	private LinearLayout mDateInfoLl;
	private LinearLayout mBottomDivLl;
	private LinearLayout mAuthLl;

	private RelativeLayout mInvoiceRl;
	private RelativeLayout mAreaRl;
	private RelativeLayout mTimeRl;
	private RelativeLayout mDateRl;

	private TextView mAreaTv;
	private TextView mTimeTv;
	private TextView mDateTv;
	private TextView mAreaTagTv;
	private TextView mTimeTagTv;
	private TextView mDateTagTv;
	private TextView mTimingTv;
	private TextView mAgreementTv;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private EditText mAddressEt;
	private EditText mInvoiceTitleEt;
	private EditText mInvoiceContentEt;

	private CheckBox mInvoiceCb;
	private CheckBox mInTimeCb;
	private ImageView mBackIv;

	private String mLat = "0";
	private String mLon = "0";

	private String mNowAction = ORIGIN_FROM_MAIN_ACTION;

	private String mPhone;
	private String mAuthCode;

	private int mTiming = 60;

	public static boolean sIsNowDate = true;
	private boolean mIsIntime = true;
	private boolean mIsInvoice = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_CREATE_SUC: {
				if (ORIGIN_FROM_DETAIL_ACTION.equals(mNowAction)) {
					OrderManager.getsCurrentOrderGoodsList().addAll(
							CartManager.getsDetailBuyList());

					CartManager.getsDetailBuyList().clear();

					ActivitiyInfoManager
					.finishActivity("com.xgf.winecome.ui.activity.GoodsDetailActivity");
				} else if (ORIGIN_FROM_CART_ACTION.equals(mNowAction)) {
					OrderManager.getsCurrentOrderGoodsList().addAll(
							CartManager.getsSelectCartList());

					CartManager.removeCartSelect();
					HomeActivity.modifyCartPayView("0", "0");
				} else {
					OrderManager.getsCurrentOrderGoodsList().addAll(
							CartManager.getsCartList());

					CartManager.getsCartList().clear();
					CartManager.getsSelectCartList().clear();
					CartManager.showOrhHidePayBar(false);

					HomeActivity.modifyMainPayView("0", false);
				}

				Intent intent = new Intent(PersonInfoActivity.this,
						PayActivity.class);
				startActivity(intent);
				PersonInfoActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				break;
			}
			case OrderLogic.ORDER_CREATE_FAIL: {

				break;
			}
			case OrderLogic.ORDER_CREATE_EXCEPTION: {
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

	Handler mAuthCodeHandler = new Handler() {

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
					mAuthCodeLl.setBackgroundColor(getResources().getColor(
							R.color.orange_bg));
					mTimingTv
							.setText(getString(R.string.get_verification_code));
					mAuthCodeLl.setClickable(true);
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
		setContentView(R.layout.person_info_form);
		mContext = PersonInfoActivity.this;
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mAuthLl = (LinearLayout) findViewById(R.id.per_info_auth_ll);
		mAuthCodeLl = (LinearLayout) findViewById(R.id.per_info_ver_code_ll);
		mSubmitLl = (LinearLayout) findViewById(R.id.per_info_submit_ll);
		mInvoiceLl = (LinearLayout) findViewById(R.id.per_info_invoice_ll);
		mBottomLl = (LinearLayout) findViewById(R.id.per_info_bottom_ll);
		mBottomDivLl = (LinearLayout) findViewById(R.id.per_info_bottom_division_ll);
		mInvoiceInfoLl = (LinearLayout) findViewById(R.id.per_info_invoice_info_ll);
		mDateInfoLl = (LinearLayout) findViewById(R.id.per_info_date_info_ll);

		mAreaRl = (RelativeLayout) findViewById(R.id.per_info_area_rl);
		mTimeRl = (RelativeLayout) findViewById(R.id.per_info_time_rl);
		mDateRl = (RelativeLayout) findViewById(R.id.per_info_date_rl);
		mInvoiceRl = (RelativeLayout) findViewById(R.id.per_info_invoice_rl);

		mAreaTagTv = (TextView) findViewById(R.id.per_info_area_tag_tv);
		mDateTagTv = (TextView) findViewById(R.id.per_info_date_tag_tv);
		mTimeTagTv = (TextView) findViewById(R.id.per_info_time_tag_tv);

		mAreaTv = (TextView) findViewById(R.id.per_info_area_tv);
		mTimeTv = (TextView) findViewById(R.id.per_info_time_tv);
		mDateTv = (TextView) findViewById(R.id.per_info_date_tv);

		mTimingTv = (TextView) findViewById(R.id.per_info_ver_code_btn_tv);
		mAgreementTv = (TextView) findViewById(R.id.per_info_agreement_tv);

		mPhoneEt = (EditText) findViewById(R.id.per_info_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.per_info_ver_code_et);
		mAddressEt = (EditText) findViewById(R.id.per_info_address_et);
		mInvoiceTitleEt = (EditText) findViewById(R.id.per_info_invoice_title_et);
		mInvoiceContentEt = (EditText) findViewById(R.id.per_info_invoice_content_et);

		mInvoiceCb = (CheckBox) findViewById(R.id.per_info_invoice_cb);
		mInTimeCb = (CheckBox) findViewById(R.id.per_info_intime_cb);
		mBackIv = (ImageView) findViewById(R.id.per_info_back_iv);
	}

	private void setUpListener() {
		mAuthCodeLl.setOnClickListener(this);
		mSubmitLl.setOnClickListener(this);
		mInvoiceLl.setOnClickListener(this);
		mAreaRl.setOnClickListener(this);
		mTimeRl.setOnClickListener(this);
		mDateRl.setOnClickListener(this);
		mInvoiceRl.setOnClickListener(this);
		mAreaTv.setOnClickListener(this);
		mTimeTv.setOnClickListener(this);
		mDateTv.setOnClickListener(this);
		mAreaTagTv.setOnClickListener(this);
		mDateTagTv.setOnClickListener(this);
		mTimeTagTv.setOnClickListener(this);
		mAgreementTv.setOnClickListener(this);

		// mPhoneEt.addTextChangedListener(this);
		// mVerCodeEt.addTextChangedListener(this);
		// mAddressEt.addTextChangedListener(this);
		// mInvoiceTitleEt.addTextChangedListener(this);
		// mInvoiceContentEt.addTextChangedListener(this);

		mInvoiceCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (!isChecked) {
					mInvoiceInfoLl.setVisibility(View.GONE);
					mBottomDivLl.setVisibility(View.VISIBLE);
					mIsInvoice = false;
				} else {
					mInvoiceInfoLl.setVisibility(View.VISIBLE);
					mBottomDivLl.setVisibility(View.GONE);
					mIsInvoice = true;
				}

			}
		});

		mInTimeCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mDateInfoLl.setVisibility(View.GONE);
					mIsIntime = true;
				} else {
					mDateInfoLl.setVisibility(View.VISIBLE);
					mIsIntime = false;
				}

			}
		});

		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
		// if (!UserInfoManager.getIsMustAuth(mContext)) {
		// //mAuthLl.setVisibility(View.GONE);
		//
		// }
		mPhone = UserInfoManager.getPhone(mContext);
		mPhoneEt.setText(mPhone);
		getLoc();
		mNowAction = getIntent().getAction();
	}

	private void updateShow() {
		if (TextUtils.isEmpty(mPhoneEt.getText().toString().trim())) {
			mPhoneEt.setError(getString(R.string.mobile_phone_hint));
		}
		if (TextUtils.isEmpty(mVerCodeEt.getText().toString().trim())) {
			mVerCodeEt.setError(getString(R.string.verification_code_hint));
		}
		if (TextUtils.isEmpty(mAddressEt.getText().toString().trim())) {
			mAddressEt.setError(getString(R.string.detail_info_hint));
		}
		if (TextUtils.isEmpty(mInvoiceTitleEt.getText().toString().trim())) {
			mInvoiceTitleEt.setError(getString(R.string.invoice_title_hint));
		}
		if (TextUtils.isEmpty(mInvoiceContentEt.getText().toString().trim())) {
			mInvoiceContentEt
					.setError(getString(R.string.invoice_content_hint));
		}
	}

	private void getLoc() {
		LocationUtilsV5.getLocation(getApplicationContext(),
				new LocationCallback() {
					@Override
					public void onGetLocation(BDLocation location) {
						Log.e("xxx_latitude", "" + location.getLatitude());
						Log.e("xxx_longitude", "" + location.getLongitude());

						mLat = String.valueOf(location.getLatitude());
						mLon = String.valueOf(location.getLongitude());
						String addr = location.getAddrStr();
						if (!TextUtils.isEmpty(addr)) {
							if (addr.contains("省")) {
								int index = addr.indexOf("省");
								addr = addr.substring(index + 1);
								Log.e("xxx_addr", "" + addr);
								mAddressEt.setText(addr);
							} else {
								mAddressEt.setText(addr);
							}
						}
					}
				});
	}

	private void submitCreateOrder() {
		Order order = new Order();
		mPhone = mPhoneEt.getText().toString().trim();
		order.setPhone(mPhone);
		if (TextUtils.isEmpty(mVerCodeEt.getText().toString().trim())) {
			Toast.makeText(mContext,
					getString(R.string.verification_code_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		// address
		if (TextUtils.isEmpty(mAddressEt.getText().toString().trim())) {
			Toast.makeText(mContext, getString(R.string.detail_info_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		order.setAddress(mAddressEt.getText().toString().trim());
		order.setLatitude(mLat);
		order.setLongitude(mLon);

		// inTime
		if (!mIsIntime
				&& (TextUtils.isEmpty(mDateTv.getText()) || TextUtils
						.isEmpty(mDateTv.getText() + " " + mTimeTv.getText()))) {
			Toast.makeText(mContext,
					getString(R.string.distribution_time_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!mIsIntime) {
			order.setDeliveryTime(mDateTv.getText() + " " + mTimeTv.getText());
		} else {
			String date = TimeUtils
					.TimeStamp2Date(
							String.valueOf(System.currentTimeMillis() + 20 * 60 * 1000),
							TimeUtils.FORMAT_PATTERN_DATE);
			order.setDeliveryTime(date);
		}

		// invoice
		order.setInvoice(String.valueOf(mIsInvoice));

		if (mIsInvoice
				&& (TextUtils.isEmpty(mInvoiceTitleEt.getText().toString()
						.trim()) || TextUtils.isEmpty(mInvoiceContentEt
						.getText().toString().trim()))) {
			Toast.makeText(mContext, getString(R.string.invoice_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		order.setInvoiceTitle(mInvoiceTitleEt.getText().toString().trim());
		order.setInvoiceContent(mInvoiceContentEt.getText().toString().trim());

		order.setPayWay("");

		// TODO
		if (ORIGIN_FROM_DETAIL_ACTION.equals(mNowAction)) {
			OrderLogic.createOrder(mContext, mHandler, order,
					CartManager.getsDetailBuyList());
		} else if (ORIGIN_FROM_CART_ACTION.equals(mNowAction)) {
			OrderLogic.createOrder(mContext, mHandler, order,
					CartManager.getsSelectCartList());
		} else {
			OrderLogic.createOrder(mContext, mHandler, order,
					CartManager.getsCartList());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 500: {
				mAreaTv.setText(data.getStringExtra("area"));
				break;
			}
			case 501: {
				mDateTv.setText(data.getStringExtra("date_value"));
				break;
			}
			case 502: {
				mTimeTv.setText(data.getStringExtra("time_value"));
				break;
			}
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		updateShow();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.per_info_area_rl:
		case R.id.per_info_area_tv:
		case R.id.per_info_area_tag_tv: {
			Intent intent = new Intent(PersonInfoActivity.this,
					AreaSelectActivity.class);
			startActivityForResult(intent, 500);
			break;
		}
		case R.id.per_info_date_rl:
		case R.id.per_info_date_tv:
		case R.id.per_info_date_tag_tv: {
			Intent intent = new Intent(PersonInfoActivity.this,
					DateSelectActivity.class);
			startActivityForResult(intent, 501);
			break;
		}
		case R.id.per_info_time_rl:
		case R.id.per_info_time_tv:
		case R.id.per_info_time_tag_tv: {
			if (!TextUtils.isEmpty(mDateTv.getText().toString().trim())) {
				Intent intent = new Intent(PersonInfoActivity.this,
						TimeSelectActivity.class);
				startActivityForResult(intent, 502);
				break;
			} else {
				Toast.makeText(mContext, getString(R.string.date_select),
						Toast.LENGTH_SHORT).show();
			}
		}
		case R.id.per_info_submit_ll: {
			submitCreateOrder();
			break;
		}
		case R.id.per_info_back_iv: {
			PersonInfoActivity.this.finish();
			break;
		}
		case R.id.per_info_ver_code_ll: {
			mPhone = mPhoneEt.getText().toString().trim();
			if (!TextUtils.isEmpty(mPhone)) {
				UserLogic.sendAuthCode(mContext, mAuthCodeHandler, mPhone);

			} else {
				Toast.makeText(mContext, getString(R.string.mobile_phone_hint),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.per_info_agreement_tv: {
			Intent intent = new Intent(PersonInfoActivity.this,
					AgreementActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		default:
			break;
		}
	}
}
