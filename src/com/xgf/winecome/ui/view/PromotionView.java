package com.xgf.winecome.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.PromotionNew;
import com.xgf.winecome.ui.activity.GoodsDetailActivity;
import com.xgf.winecome.ui.activity.PromotionActivity;
import com.xgf.winecome.ui.adapter.MainGoodsGvAdapter;
import com.xgf.winecome.ui.adapter.MainPromotionGvAdapter;

public class PromotionView extends LinearLayout {

	private ImageView mIv;

	private RelativeLayout mCustomClassifyRl;
	private Context mContext;
	private PromotionNew mPromotion;

	private TextView mTitleNameTv;
	private TextView mFirstNameTv;
	private TextView mSecondNameTv;
	private TextView mNumTv;

	private ArrayList<PromotionNew> mPromotionList = new ArrayList<PromotionNew>();
	private CustomGridView mPromotionGv;
	private MainPromotionGvAdapter mPromotionGvAdapter;

	public PromotionView(Context context, ArrayList<PromotionNew> promotionList) {
		super(context);
		mContext = context;
		mPromotionList = promotionList;
		initView(context, promotionList);
		// fillData(context, classifyGoods);
	}

	private void initView(final Context context,
			final ArrayList<PromotionNew> promotionList) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.custom_classify, null);

		mCustomClassifyRl = (RelativeLayout) layout
				.findViewById(R.id.custom_classify_rl);
		mCustomClassifyRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PromotionActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(PromotionActivity.PROMOTION_KEY,
						mPromotion);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});

		mIv = (ImageView) layout.findViewById(R.id.custom_classify_iv);
		mTitleNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_title_tv);
		mFirstNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_first_name_tv);
		mSecondNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_second_name_tv);

		mPromotionGv = (CustomGridView) layout
				.findViewById(R.id.custom_classify_goods_gv);



		mPromotionGvAdapter = new MainPromotionGvAdapter(context, mPromotionList);
		mPromotionGv.setAdapter(mPromotionGvAdapter);
		mPromotionGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, PromotionActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(PromotionActivity.PROMOTION_KEY,
						mPromotionList.get(position));
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});

		// mFirstNameTv.setText(rootNameList.get(0).getSubname());
		// mSecondNameTv.setText(rootNameList.get(1).getSubname());
		// mTitleNameTv.setText(homeRecommend.getName());

		this.addView(layout);
	}

	public void fillData(Context context, PromotionNew classifyGoods) {

	}

}
