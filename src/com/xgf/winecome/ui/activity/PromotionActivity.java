package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.PromotionNew;
import com.xgf.winecome.network.logic.PromotionLogic;
import com.xgf.winecome.network.logic.SpecialEventLogic;
import com.xgf.winecome.ui.adapter.SpecialEventsGvAdapter;
import com.xgf.winecome.ui.view.BorderScrollView;
import com.xgf.winecome.ui.view.CustomGridView;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.ActivitiyInfoManager;

public class PromotionActivity extends Activity implements OnClickListener {

	public static final String PROMOTION_KEY = "PromotionKey";

	public static final String ORIGIN_FROM_PUSH_ACTION = "PUSH";

	public static final String ORIGIN_FROM_MAIN_ACTION = "MAIN";

	private Context mContext;

	private CustomGridView mGoodsGv;

	private ImageView mBackIv;

	private ImageView mPromotionIv;

	private BorderScrollView mScrollView;

	private PromotionNew mPromotion;

	private SpecialEventsGvAdapter mGvAdapter;

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private String mNowAction = ORIGIN_FROM_MAIN_ACTION;

	protected CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case PromotionLogic.PROMOTION_GET_SUC: {
				if (null != msg.obj) {
					mPromotion = (PromotionNew) msg.obj;
					mGoodsList.clear();
					mGoodsList.addAll(mPromotion.getGoodsList());
					mGvAdapter.notifyDataSetChanged();

					ImageLoader.getInstance().displayImage(
							mPromotion.getDetailImg(), mPromotionIv);
				}
				break;
			}
			case PromotionLogic.PROMOTION_GET_FAIL: {
				break;
			}
			case PromotionLogic.PROMOTION_GET_EXCEPTION: {
				break;
			}

			default:
				break;
			}
			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promotion);
		mContext = PromotionActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		if (!ActivitiyInfoManager.activitityMap
				.containsKey(ActivitiyInfoManager
						.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap
					.put(ActivitiyInfoManager.getCurrentActivityName(mContext),
							this);
		}
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mScrollView = (BorderScrollView) findViewById(R.id.promotion_sv);
		mBackIv = (ImageView) findViewById(R.id.promotion_back_iv);

		mPromotionIv = (ImageView) findViewById(R.id.promotion_iv);
		mGoodsGv = (CustomGridView) findViewById(R.id.promotion_gv);

		mGvAdapter = new SpecialEventsGvAdapter(PromotionActivity.this,
				mGoodsList);
		mGoodsGv.setAdapter(mGvAdapter);
	}

	private void setUpListener() {
		mBackIv.setOnClickListener(this);
		mGoodsGv.clearFocus();
		mGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(PromotionActivity.this,
						GoodsDetailActivity.class);
				intent.setAction(GoodsDetailActivity.ORIGIN_FROM_PROMOTION_ACTION);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_KEY,
						mGoodsList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	private void setUpData() {
		mNowAction = getIntent().getAction();
		if (ORIGIN_FROM_MAIN_ACTION.equals(mNowAction)) {
			mPromotion = (PromotionNew) getIntent().getSerializableExtra(
					PromotionActivity.PROMOTION_KEY);
			mGoodsList.clear();
			mGoodsList.addAll(mPromotion.getGoodsList());
			mGvAdapter.notifyDataSetChanged();

			ImageLoader.getInstance().displayImage(mPromotion.getDetailImg(),
					mPromotionIv);
		} else if (ORIGIN_FROM_PUSH_ACTION.equals(mNowAction)) {
			mCustomProgressDialog.show();
			String id = getIntent().getStringExtra("id");
			PromotionLogic.getPromotionById(mContext, mHandler, id, "0", "15");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mScrollView.smoothScrollTo(0, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.promotion_back_iv: {
			ActivitiyInfoManager
					.finishActivity("com.xgf.winecome.ui.activity.PromotionActivity");
			finish();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		ActivitiyInfoManager
				.finishActivity("com.xgf.winecome.ui.activity.PromotionActivity");
		finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
}
