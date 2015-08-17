package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.adapter.CartGoodsAdapter;
import com.xgf.winecome.ui.view.listview.SwipeMenu;
import com.xgf.winecome.ui.view.listview.SwipeMenuCreator;
import com.xgf.winecome.ui.view.listview.SwipeMenuItem;
import com.xgf.winecome.ui.view.listview.SwipeMenuListView;
import com.xgf.winecome.utils.CartManager;

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
import android.widget.TextView;

public class ShopCartActivity extends Activity implements OnClickListener {

	private Context mContext;

	private TextView mTotalNumTv;

	private SwipeMenuListView mGoodsLv;

	public static ArrayList<Goods> sGoodsList = new ArrayList<Goods>();

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private static CartGoodsAdapter mGoodsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mContext = ShopCartActivity.this;
		mTotalNumTv = (TextView) findViewById(R.id.shop_cart_total_num_tv);
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

					mGoodsList.remove(position);
					CartManager.cartRemove(position);
					// del
					for (int i = 0; i < mGoodsList.size(); i++) {
						if (i < position) {
							mGoodsAdapter.getmIsSelected().put(i,
									mGoodsAdapter.getmIsSelected().get(i));

						} else {
							mGoodsAdapter.getmIsSelected().put(i,
									mGoodsAdapter.getmIsSelected().get(i + 1));
						}

					}
					mGoodsAdapter.getmIsSelected()
							.remove(mGoodsList.size() + 1);
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
		mGoodsList.clear();
		mGoodsList.addAll(CartManager.getsCartList());
		mGoodsAdapter.initCheck();
		mGoodsAdapter.notifyDataSetChanged();
		mTotalNumTv.setText("(" + String.valueOf(mGoodsList.size()) + ")");

	}

	public static void refreshView(boolean isChecked) {
		if (isChecked) {
			mGoodsAdapter.initChecked();
		} else {
			mGoodsAdapter.initCheck();
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
