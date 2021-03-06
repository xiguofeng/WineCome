package com.xgf.winecome.ui.activity;

import com.xgf.winecome.R;
import com.xgf.winecome.network.logic.CommentLogic;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.ActivitiyInfoManager;
import com.xgf.winecome.utils.OrderManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CommentsActivity extends Activity implements OnClickListener {

	private Context mContext;

	private LinearLayout mVeryGoodLl;

	private LinearLayout mGoodLl;

	private LinearLayout mNotGoodLl;

	private ImageView mBackIv;

	protected CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CommentLogic.COMMENT_ADD_SUC: {
				ActivitiyInfoManager
						.finishActivity("com.xgf.winecome.ui.activity.QrResultActivity");
				// AppManager.getInstance().killActivity(QrResultActivity.class);
				Intent intent = new Intent(CommentsActivity.this,
						CommentsResultActivity.class);
				startActivity(intent);
				CommentsActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);

				break;
			}
			case CommentLogic.COMMENT_ADD_FAIL: {
				// Toast.makeText(mContext, R.string.login_fail,
				// Toast.LENGTH_SHORT).show();
				break;
			}
			case CommentLogic.COMMENT_ADD_EXCEPTION: {
				break;
			}
			case CommentLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.show();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);
		mContext = CommentsActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
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
			if (!TextUtils.isEmpty(OrderManager.getsCurrentCommentOrderId())) {
				if (null != mCustomProgressDialog) {
					mCustomProgressDialog.show();
				}
				CommentLogic.addComment(CommentsActivity.this, mHandler,
						OrderManager.getsCurrentCommentOrderId(),
						getString(R.string.comments_very_good));
			} else {

			}
			break;
		}
		case R.id.comments_good_ll: {
			if (!TextUtils.isEmpty(OrderManager.getsCurrentCommentOrderId())) {
				CommentLogic.addComment(CommentsActivity.this, mHandler,
						OrderManager.getsCurrentCommentOrderId(),
						getString(R.string.comments_good));
			} else
				break;
		}
		case R.id.comments_not_good_ll: {
			if (!TextUtils.isEmpty(OrderManager.getsCurrentCommentOrderId())) {
				CommentLogic.addComment(CommentsActivity.this, mHandler,
						OrderManager.getsCurrentCommentOrderId(),
						getString(R.string.comments_not_good));
			} else {

			}
			break;
		}

		default:
			break;
		}
	}

}
