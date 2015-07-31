package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.ui.adapter.OrderWineAdapter;

public class OrderListActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ListView mOrderLv;
	private OrderWineAdapter mOrderAdapter;

	private ArrayList<Order> orderList = new ArrayList<Order>();
	private List<String> parent = null;
	private Map<String, List<String>> map = null;

	private ImageView mBackIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		mContext = OrderListActivity.this;
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initData();
	}

	private void initView() {
		mContext = OrderListActivity.this;
		mOrderLv = (ListView) findViewById(R.id.order_list_lv);
		mOrderAdapter = new OrderWineAdapter(mContext, orderList);
		mOrderLv.setAdapter(mOrderAdapter);

		mBackIv = (ImageView) findViewById(R.id.order_list_back_iv);
		// mOrderLv.setAdapter(new OrderExAdapter());
		mBackIv.setOnClickListener(this);
	}

	private void initData() {

		orderList.clear();
		for (int i = 0; i < 9; i++) {
			Order order = new Order();
			order.setId("订单" + i);
			order.setTime("2015-7-20 19:30");
			order.setState("配送");
			orderList.add(order);
		}
		mOrderAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_list_back_iv: {
			finish();
			break;
		}
		default:
			break;
		}
	}

}
