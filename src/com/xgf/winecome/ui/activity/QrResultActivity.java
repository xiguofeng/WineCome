package com.xgf.winecome.ui.activity;

import com.xgf.winecome.R;
import com.xgf.winecome.qrcode.google.zxing.client.CaptureActivity;
import com.xgf.winecome.utils.ActivitiyInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class QrResultActivity extends Activity implements OnClickListener {

	private Context mContext;

	private LinearLayout mQueryOrderLl;

	private LinearLayout mGoOnLl;

	private LinearLayout mOrderCommentLl;

	private ImageView mBackIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_result);
		mContext = QrResultActivity.this;
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mQueryOrderLl = (LinearLayout) findViewById(R.id.qr_result_order_search_ll);
		mGoOnLl = (LinearLayout) findViewById(R.id.qr_result_go_on_ll);
		mOrderCommentLl = (LinearLayout) findViewById(R.id.qr_result_order_comment_ll);

		mBackIv = (ImageView) findViewById(R.id.qr_result_back_iv);
	}

	private void setUpListener() {
		mQueryOrderLl.setOnClickListener(this);
		mGoOnLl.setOnClickListener(this);
		mOrderCommentLl.setOnClickListener(this);
		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.qr_result_back_iv: {
			finish();
			break;
		}

		case R.id.qr_result_go_on_ll: {
			if (!ActivitiyInfoManager.activitityMap
					.containsKey(ActivitiyInfoManager
							.getCurrentActivityName(mContext))) {
				ActivitiyInfoManager.activitityMap.put(
						ActivitiyInfoManager.getCurrentActivityName(mContext),
						this);
			}
			Intent intent = new Intent(QrResultActivity.this,
					CaptureActivity.class);
			intent.setAction(CaptureActivity.ORIGIN_FROM_QR_RESULT_ACTION);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
			if (!ActivitiyInfoManager.activitityMap
					.containsKey(ActivitiyInfoManager
							.getCurrentActivityName(mContext))) {
				ActivitiyInfoManager.activitityMap.put(
						ActivitiyInfoManager.getCurrentActivityName(mContext),
						this);
			}
			Intent intent = new Intent(QrResultActivity.this,
					CommentsActivity.class);

			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		default:
			break;
		}
	}

}
