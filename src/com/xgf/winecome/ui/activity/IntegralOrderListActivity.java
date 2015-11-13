package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.IntegralGoods;
import com.xgf.winecome.network.config.MsgRequest;
import com.xgf.winecome.network.logic.IntegralGoodsLogic;
import com.xgf.winecome.network.logic.UserLogic;
import com.xgf.winecome.ui.adapter.IntegralOrderAdapter;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.UserInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class IntegralOrderListActivity extends Activity implements
		OnClickListener {

	private Context mContext;

	private ImageView mBackIv;
	
	private ListView mOrderLv;

	private IntegralOrderAdapter mAdapter;

	private ArrayList<IntegralGoods> mIntegralGoodsList = new ArrayList<IntegralGoods>();

	protected CustomProgressDialog2 mCustomProgressDialog;

	private String mPhone;

	private int pageNum;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case IntegralGoodsLogic.INTEGRAL_ORDER_LIST_GET_SUC: {
				if (null != msg.obj) {
					mIntegralGoodsList.clear();
					mIntegralGoodsList
							.addAll((Collection<? extends IntegralGoods>) msg.obj);
					mAdapter.notifyDataSetChanged();
				}
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_ORDER_LIST_GET_FAIL: {
				Toast.makeText(mContext, "积分订单获取失败", Toast.LENGTH_SHORT).show();
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_ORDER_LIST_GET_EXCEPTION: {
				break;
			}
			case IntegralGoodsLogic.NET_ERROR: {
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
		setContentView(R.layout.integral_order_list);
		mContext = IntegralOrderListActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
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
		mBackIv= (ImageView) findViewById(R.id.integral_order_list_back_iv);
		
		mOrderLv = (ListView) findViewById(R.id.integral_order_list_lv);
		mAdapter = new IntegralOrderAdapter(mContext, mIntegralGoodsList);
		mOrderLv.setAdapter(mAdapter);
		// mOrderLv.setAdapter(new OrderExAdapter());
	}

	private void initData() {
		mPhone = getIntent().getStringExtra("phone");
		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		IntegralGoodsLogic.getExchangeOrder(mContext, mHandler, mPhone,
				String.valueOf(pageNum), String.valueOf(MsgRequest.PAGE_SIZE));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.integral_order_list_back_iv: {
			finish();
			break;
		}
		default:
			break;
		}
	}

}
