package com.xgf.winecome.ui.activity;

import com.xgf.winecome.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CommentsResultActivity extends Activity implements OnClickListener {

	private LinearLayout mGoOnMallLl;

	private LinearLayout mOrderQueryLl;

	private ImageView mBackIv;

	private final int DISPLAY_LENGTH = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments_result);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mGoOnMallLl = (LinearLayout) findViewById(R.id.comments_result_go_on_ll);
		mOrderQueryLl = (LinearLayout) findViewById(R.id.comments_result_order_search_ll);
		mBackIv = (ImageView) findViewById(R.id.comments_result_back_iv);

	}

	private void setUpListener() {
		mGoOnMallLl.setOnClickListener(this);
		mOrderQueryLl.setOnClickListener(this);
		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {

		// 启动三秒后进度到登陆界面
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(CommentsResultActivity.this,
						MainActivity.class);
				startActivity(intent);
				CommentsResultActivity.this.finish();
			}
		}, DISPLAY_LENGTH);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.comments_result_back_iv: {
			CommentsResultActivity.this.finish();
			break;
		}
		case R.id.comments_result_order_search_ll: {
			Intent intent = new Intent(CommentsResultActivity.this,
					OrderQueryActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.comments_result_go_on_ll: {
			Intent intent = new Intent(CommentsResultActivity.this,
					IntegralMallActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		default:
			break;
		}
	}

}
