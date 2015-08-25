package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.logic.OrderLogic;
import com.xgf.winecome.ui.adapter.OrderWineAdapter;
import com.xgf.winecome.ui.utils.ListItemClickHelp;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.OrderManager;

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

public class OrderListActivity extends Activity implements OnClickListener,
		ListItemClickHelp {

	private Context mContext;

	private ListView mOrderLv;
	private OrderWineAdapter mOrderAdapter;

	private ArrayList<Order> orderList = new ArrayList<Order>();
	private HashMap<String, Object> mMsgMap = new HashMap<String, Object>();

	private ImageView mBackIv;

	private String mPhone;
	protected CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDERLIST_GET_SUC: {
				mMsgMap.clear();
				mMsgMap.putAll((Map<? extends String, ? extends Object>) msg.obj);
				mOrderAdapter.notifyDataSetChanged();
				break;
			}
			case OrderLogic.ORDERLIST_GET_FAIL: {

				break;
			}
			case OrderLogic.ORDERLIST_GET_EXCEPTION: {
				break;
			}

			case OrderLogic.ORDER_CANCEL_SUC: {
				Toast.makeText(mContext, getString(R.string.order_cancel_suc),
						Toast.LENGTH_SHORT).show();
				OrderLogic.getOrders(mContext, mHandler, mPhone, "0", "30");
				break;
			}
			case OrderLogic.ORDER_CANCEL_FAIL: {
				Toast.makeText(mContext, getString(R.string.order_cancel_fail),
						Toast.LENGTH_SHORT).show();
				break;
			}
			case OrderLogic.ORDER_CANCEL_EXCEPTION: {
				break;
			}
			case OrderLogic.NET_ERROR: {
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
		setContentView(R.layout.order_list);
		mContext = OrderListActivity.this;
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
		mContext = OrderListActivity.this;
		mOrderLv = (ListView) findViewById(R.id.order_list_lv);
		mOrderAdapter = new OrderWineAdapter(mContext, mMsgMap, this);
		mOrderLv.setAdapter(mOrderAdapter);

		mBackIv = (ImageView) findViewById(R.id.order_list_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {
		mPhone = getIntent().getStringExtra("phone");
		// mPhone = UserInfoManager.getPhone(mContext);
		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		OrderLogic.getOrders(mContext, mHandler, mPhone, "0", "20");
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

	@Override
	public void onClick(View item, View widget, int position, int which) {
		switch (which) {
		case R.id.list_order_group_see_btn: {
			OrderManager.setsCurrentCommentOrderId(((ArrayList<Order>) mMsgMap
					.get(MsgResult.ORDER_TAG)).get(position).getId());
			Intent intent = new Intent(mContext, OrderStateActivity.class);
			intent.putExtra(
					"order_state",
					((ArrayList<Order>) mMsgMap.get(MsgResult.ORDER_TAG)).get(
							position).getOrderStatus());
			intent.putExtra(
					"delivery_time",
					((ArrayList<Order>) mMsgMap.get(MsgResult.ORDER_TAG)).get(
							position).getDeliveryTime());

			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.list_order_group_del_or_cancel_btn: {
			if (Integer.parseInt(((ArrayList<Order>) mMsgMap
					.get(MsgResult.ORDER_TAG)).get(position).getOrderStatus()) < 4) {
				if (null != mCustomProgressDialog) {
					mCustomProgressDialog.show();
				}
				OrderLogic.cancelOrder(mContext, mHandler,
						((ArrayList<Order>) mMsgMap.get(MsgResult.ORDER_TAG))
								.get(position).getId());
			} else {
				Toast.makeText(mContext, getString(R.string.order_cancel_no),
						Toast.LENGTH_SHORT).show();
			}

			break;
		}
		default:
			break;
		}
	}

}
