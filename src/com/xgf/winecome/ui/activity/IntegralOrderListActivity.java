package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.IntegralGoods;
import com.xgf.winecome.ui.adapter.IntegralOrderAdapter;

public class IntegralOrderListActivity extends Activity implements
		OnClickListener {

	private Context mContext;

	private ListView mOrderLv;

	private IntegralOrderAdapter mAdapter;

	private ArrayList<IntegralGoods> mIntegralGoodsList = new ArrayList<IntegralGoods>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_order_list);
		mContext = IntegralOrderListActivity.this;
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initData();
	}

	private void initView() {
		mContext = IntegralOrderListActivity.this;
		mOrderLv = (ListView) findViewById(R.id.integral_order_list_lv);
		mAdapter = new IntegralOrderAdapter(mContext, mIntegralGoodsList);
		mOrderLv.setAdapter(mAdapter);
		// mOrderLv.setAdapter(new OrderExAdapter());
	}

	private void initData() {
		mIntegralGoodsList.clear();
		for (int i = 0; i < 10; i++) {
			IntegralGoods integralGoods = new IntegralGoods();
			integralGoods.setName("兑换商品" + i);
			integralGoods.setIntegral("" + i + "积分");
			mIntegralGoodsList.add(integralGoods);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
	}

}
