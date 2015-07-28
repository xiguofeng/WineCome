package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xgf.winecome.R;

public class CommentsActivity extends Activity implements OnClickListener {

	private LinearLayout mVeryGoodLl;

	private LinearLayout mGoodLl;

	private LinearLayout mNotGoodLl;

	private ImageView mBackIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mVeryGoodLl = (LinearLayout) findViewById(R.id.comments_very_good_ll);
		mGoodLl = (LinearLayout) findViewById(R.id.comments_good_ll);
		mNotGoodLl = (LinearLayout) findViewById(R.id.comments_not_good_ll);

		mBackIv = (ImageView) findViewById(R.id.comments_back_iv);
	}

	private void setUpListener() {
		mVeryGoodLl.setOnClickListener(this);
		mGoodLl.setOnClickListener(this);
		mNotGoodLl.setOnClickListener(this);

		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comments_back_iv: {
			CommentsActivity.this.finish();
			break;
		}
		case R.id.comments_very_good_ll: {
			Intent intent = new Intent(CommentsActivity.this,
					CommentsResultActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.comments_good_ll: {
			Intent intent = new Intent(CommentsActivity.this,
					CommentsResultActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.comments_not_good_ll: {
			Intent intent = new Intent(CommentsActivity.this,
					CommentsResultActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		default:
			break;
		}
	}

}
