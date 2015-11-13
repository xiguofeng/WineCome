package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.PromotionNew;
import com.xgf.winecome.network.logic.SpecialEventLogic;
import com.xgf.winecome.ui.adapter.SpecialEventsGvAdapter;
import com.xgf.winecome.ui.view.CustomGridView;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.ActivitiyInfoManager;

public class PromotionActivity extends Activity implements OnClickListener {

	public static final String PROMOTION_KEY = "PromotionKey";

	private Context mContext;

	private CustomGridView mGoodsGv;

	private ImageView mBackIv;
	
	private ImageView mPromotionIv;

	private PromotionNew mPromotion;

	private SpecialEventsGvAdapter mGvAdapter;

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	protected CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case SpecialEventLogic.GOODS_LIST_GET_SUC: {
				if (null != msg.obj) {
					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) msg.obj);
					mGvAdapter.notifyDataSetChanged();
				}
				break;
			}
			case SpecialEventLogic.GOODS_LIST_GET_FAIL: {
				break;
			}
			case SpecialEventLogic.GOODS_LIST_GET_EXCEPTION: {
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
		mBackIv = (ImageView) findViewById(R.id.promotion_back_iv);
		
		mPromotionIv= (ImageView) findViewById(R.id.promotion_iv);
		mGoodsGv = (CustomGridView) findViewById(R.id.promotion_gv);

		mGvAdapter = new SpecialEventsGvAdapter(PromotionActivity.this,
				mGoodsList);
		mGoodsGv.setAdapter(mGvAdapter);
	}

	private void setUpListener() {
		mBackIv.setOnClickListener(this);
		mGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(PromotionActivity.this,
						GoodsDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_KEY,
						mGoodsList.get(position));
				intent.putExtras(bundle);
				intent.setAction(GoodsDetailActivity.ORIGIN_FROM_PROMOTION_ACTION);
				startActivity(intent);
			}
		});
	}

	private void setUpData() {
		mPromotion = (PromotionNew) getIntent().getSerializableExtra(
				PromotionActivity.PROMOTION_KEY);

		ImageLoader.getInstance().displayImage(mPromotion.getDetailImg(), mPromotionIv);
		mGoodsList.clear();
		mGoodsList.addAll(mPromotion.getGoodsList());
		mGvAdapter.notifyDataSetChanged();
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
			finish();
			break;
		}
		default:
			break;
		}
	}
}
