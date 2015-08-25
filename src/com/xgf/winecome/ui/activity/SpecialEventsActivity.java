package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.network.logic.IntegralGoodsLogic;
import com.xgf.winecome.ui.adapter.SpecialEventsGvAdapter;
import com.xgf.winecome.ui.view.CustomProgressDialog;
import com.xgf.winecome.ui.view.CustomProgressDialog2;

public class SpecialEventsActivity extends Activity implements OnClickListener {

	private Context mContext;

	private GridView mIntegralGoodsGv;

	private ImageView mBackIv;

	private SpecialEventsGvAdapter mGvAdapter;

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	protected CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case IntegralGoodsLogic.INTEGRAL_GOODS_LIST_GET_SUC: {
				if (null != msg.obj) {

				}
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_GOODS_LIST_GET_FAIL: {
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_GOODS_LIST_GET_EXCEPTION: {
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
		setContentView(R.layout.special_events);
		mContext = SpecialEventsActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mBackIv = (ImageView) findViewById(R.id.special_events_back_iv);
		mIntegralGoodsGv = (GridView) findViewById(R.id.special_events_gv);

		mGvAdapter = new SpecialEventsGvAdapter(SpecialEventsActivity.this,
				mGoodsList);
		mIntegralGoodsGv.setAdapter(mGvAdapter);
	}

	private void setUpListener() {
		mBackIv.setOnClickListener(this);
		mIntegralGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	private void setUpData() {

		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		// IntegralGoodsLogic.getAllIntegralGoods(mContext, mHandler);

		mGoodsList.clear();
		for (int i = 0; i < 10; i++) {
			Goods goods = new Goods();
			goods.setName("酒" + i);
			goods.setSalesPrice("￥+109");
			mGoodsList.add(goods);
		}
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
		case R.id.special_events_back_iv: {
			finish();
			break;
		}
		default:
			break;
		}
	}
}
