package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.network.logic.UserLogic;

public class OrderQueryActivity extends Activity implements OnClickListener,
		TextWatcher {
	private LinearLayout mQueryLl;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private Context mContext;

	private ImageView mBackIv;
	// 登陆装填提示handler更新主线程，提示登陆状态情况
	Handler mLoginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.LOGIN_SUC: {
				Intent intent = new Intent(OrderQueryActivity.this,
						OrderListActivity.class);
				startActivity(intent);
				OrderQueryActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);

				break;
			}
			case UserLogic.LOGIN_FAIL: {
				Toast.makeText(mContext, R.string.login_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.LOGIN_EXCEPTION: {
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

		mPhoneEt = (EditText) findViewById(R.id.order_query_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.order_query_ver_code_et);

		mBackIv = (ImageView) findViewById(R.id.order_query_back_iv);
	}

	private void setUpListener() {
		mQueryLl.setOnClickListener(this);

		mPhoneEt.addTextChangedListener(this);
		mVerCodeEt.addTextChangedListener(this);

		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
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
			
			Intent intent = new Intent(OrderQueryActivity.this,
					OrderListActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

//			User user = new User();
//			UserLogic.login(mContext, mLoginHandler, user);

			break;
		}
		case R.id.order_query_back_iv: {

			finish();
			break;
		}

		default:
			break;
		}
	}

}
