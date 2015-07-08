package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.adapter.CartGoodsAdapter;
import com.xgf.winecome.ui.adapter.GoodsAdapter;
import com.xgf.winecome.ui.view.listview.SwipeMenu;
import com.xgf.winecome.ui.view.listview.SwipeMenuCreator;
import com.xgf.winecome.ui.view.listview.SwipeMenuItem;
import com.xgf.winecome.ui.view.listview.SwipeMenuListView;

public class ShopCartActivity extends Activity implements OnClickListener {

	private Context mContext;

	private SwipeMenuListView mGoodsLv;

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private CartGoodsAdapter mGoodsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart);
		initView();
		initData();
	}

	private void initView() {
		mContext = ShopCartActivity.this;
		mGoodsLv = (SwipeMenuListView) findViewById(R.id.shop_cart_order_lv);
		mGoodsAdapter = new CartGoodsAdapter(mContext, mGoodsList);
		mGoodsLv.setAdapter(mGoodsAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
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
					mGoodsAdapter.notifyDataSetChanged();
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
				Intent intent = new Intent(ShopCartActivity.this,
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
		for (int i = 0; i < 20; i++) {
			Goods goods = new Goods();
			goods.setId("" + i);
			goods.setName("酒" + i);
			goods.setPrice("" + i);
			goods.setNum("1");
			mGoodsList.add(goods);
		}

		mGoodsAdapter.notifyDataSetChanged();

	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onClick(View v) {
	}

}
