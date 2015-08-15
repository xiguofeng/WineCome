package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.IntegralGoods;
import com.xgf.winecome.network.logic.IntegralGoodsLogic;
import com.xgf.winecome.ui.adapter.IntegralGoodsGvAdapter;
import com.xgf.winecome.ui.view.CustomProgressDialog;

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
import android.widget.RelativeLayout;

public class IntegralMallActivity extends Activity implements OnClickListener {

	private Context mContext;

	private GridView mIntegralGoodsGv;

	private RelativeLayout mFilterRl;

	private IntegralGoodsGvAdapter mGvAdapter;

	private ArrayList<IntegralGoods> mIntegralGoodsList = new ArrayList<IntegralGoods>();

	private CustomProgressDialog mProgressDialog;

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
			mProgressDialog.dismiss();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_mall);
		mContext = IntegralMallActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mFilterRl = (RelativeLayout) findViewById(R.id.integral_mall_filter_rl);
		mIntegralGoodsGv = (GridView) findViewById(R.id.integral_mall_gv);

		mGvAdapter = new IntegralGoodsGvAdapter(IntegralMallActivity.this,
				mIntegralGoodsList);
		mIntegralGoodsGv.setAdapter(mGvAdapter);
	}

	private void setUpListener() {
		mFilterRl.setOnClickListener(this);
		mIntegralGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	private void setUpData() {

		IntegralGoodsLogic.getAllIntegralGoods(mContext, mHandler);

		mIntegralGoodsList.clear();
		// for (int i = 0; i < 10; i++) {
		// IntegralGoods integralGoods = new IntegralGoods();
		// integralGoods.setName("兑换商品" + i);
		// integralGoods.setIntegral("" + i + "积分");
		// mIntegralGoodsList.add(integralGoods);
		// }
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
		case R.id.integral_mall_filter_rl: {
			Intent intent = new Intent(IntegralMallActivity.this,
					IntegralSelectActivity.class);
			startActivityForResult(intent, 1);
		}

		default:
			break;
		}
	}
}
