package com.xgf.winecome.ui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.GoodsDetailActivity;
import com.xgf.winecome.ui.adapter.OrderAdapter;
import com.xgf.winecome.ui.view.listview.SwipeMenu;
import com.xgf.winecome.ui.view.listview.SwipeMenuCreator;
import com.xgf.winecome.ui.view.listview.SwipeMenuItem;
import com.xgf.winecome.ui.view.listview.SwipeMenuListView;

public class ShopCartFragment extends Fragment {

	private Context mContext;

	private View mRootView;

	private SwipeMenuListView mGoodsLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private OrderAdapter mOrderAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (null != mRootView) {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (null != parent) {
				parent.removeView(mRootView);
			}
		} else {
			mRootView = inflater.inflate(R.layout.shop_cart_fragment, null);
			initView(mRootView);
			initData();
		}

		return mRootView;
	}

	private void initView(View view) {
		mContext = getActivity();
		mGoodsLv = (SwipeMenuListView) view
				.findViewById(R.id.shop_cart_order_lv);
		mOrderAdapter = new OrderAdapter(mContext, mGoodsList);
		mGoodsLv.setAdapter(mOrderAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity()
						.getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(60));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mGoodsLv.setMenuCreator(creator);

		// step 2. listener item click event
		mGoodsLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:
					// del
					mGoodsList.remove(position);
					mOrderAdapter.notifyDataSetChanged();
					break;

				}
				return false;
			}
		});

		mGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.e("xxx_mRightLv_onItemClick", "1");
				Intent intent = new Intent(getActivity(),
						GoodsDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_KEY,
						mGoodsList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	private void initData() {

		mOrderAdapter.notifyDataSetChanged();

	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}