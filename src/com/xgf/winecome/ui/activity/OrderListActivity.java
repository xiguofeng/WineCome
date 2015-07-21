package com.xgf.winecome.ui.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.xgf.winecome.R;

public class OrderListActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ListView mOrderLv;
	private List<String> parent = null;
	private Map<String, List<String>> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mContext = OrderListActivity.this;
		mOrderLv = (ListView) findViewById(R.id.order_list_lv);
		//mOrderLv.setAdapter(new OrderExAdapter());
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
	}

}
