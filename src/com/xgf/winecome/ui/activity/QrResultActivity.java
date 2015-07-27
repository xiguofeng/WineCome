package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.xgf.winecome.R;

public class QrResultActivity extends Activity implements OnClickListener {

	private LinearLayout mQueryOrderLl;

	private LinearLayout mGoOnLl;

	private LinearLayout mOrderCommentLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_result);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mQueryOrderLl = (LinearLayout) findViewById(R.id.qr_result_order_search_ll);
		mGoOnLl = (LinearLayout) findViewById(R.id.qr_result_go_on_ll);
		mOrderCommentLl = (LinearLayout) findViewById(R.id.qr_result_order_comment_ll);
	}

	private void setUpListener() {
		mQueryOrderLl.setOnClickListener(this);
		mGoOnLl.setOnClickListener(this);
		mOrderCommentLl.setOnClickListener(this);
	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.qr_result_go_on_ll: {

			break;
		}
		case R.id.qr_result_order_search_ll: {
			Intent intent = new Intent(QrResultActivity.this,
					OrderQueryActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.qr_result_order_comment_ll: {
			Intent intent = new Intent(QrResultActivity.this,
					OrderQueryActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		default:
			break;
		}
	}

}
