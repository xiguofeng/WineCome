package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Category;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.network.logic.GoodsLogic;
import com.xgf.winecome.ui.adapter.CategoryAdapter;
import com.xgf.winecome.ui.adapter.GoodsAdapter;
import com.xgf.winecome.utils.CartManager;

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;
	private LinearLayout mSearchLl;
	private ListView mLeftLv;
	private ListView mRightLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private GoodsAdapter mGoodsAdapter;
	private ArrayList<Category> mCategoryList = new ArrayList<Category>();
	private CategoryAdapter mCategoryAdapter;

	private ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();

	private LinearLayout mFirstBg;
	private float y;
	private HashMap<String, Object> mMsgMap = new HashMap<String, Object>();

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case GoodsLogic.CATEGROY_GOODS_LIST_GET_SUC: {
				if (null != msg.obj) {
					mMsgMap.clear();
					mMsgMap.putAll((Map<? extends String, ? extends Object>) msg.obj);
					mCategoryList.clear();
					mCategoryList
							.addAll((Collection<? extends Category>) mMsgMap
									.get("Category"));
					mCategoryAdapter.notifyDataSetChanged();

					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) mMsgMap
							.get(mCategoryList.get(1).getPpid()));
					mGoodsAdapter.notifyDataSetChanged();

				}
				break;
			}
			case GoodsLogic.CATEGROY_GOODS_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.CATEGROY_GOODS_LIST_GET_EXCEPTION: {
				break;
			}

			case GoodsLogic.CATEGROY_LIST_GET_SUC: {
				if (null != msg.obj) {
				}
				break;
			}
			case GoodsLogic.CATEGROY_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.CATEGROY_LIST_GET_EXCEPTION: {
				break;
			}

			case GoodsLogic.GOODS_LIST_GET_SUC: {
				if (null != msg.obj) {
					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) msg.obj);
					mGoodsAdapter.notifyDataSetChanged();
				}
				break;
			}
			case GoodsLogic.GOODS_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.GOODS_LIST_GET_EXCEPTION: {
				break;
			}

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
		initData();
	}

	private void initView() {
		mContext = MainActivity.this;
		mLeftLv = (ListView) findViewById(R.id.main_left_lv);
		mRightLv = (ListView) findViewById(R.id.main_right_lv);

		mFirstBg = (LinearLayout) findViewById(R.id.main_bg);

		mSearchLl = (LinearLayout) findViewById(R.id.main_search_rl);
		mSearchLl.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// y = mSearchLl.getY();
				// TranslateAnimation animation = new TranslateAnimation(0, 0,
				// 0,
				// -y);
				// animation.setDuration(500);
				// animation.setFillAfter(true);
				// animation.setAnimationListener(new AnimationListener() {
				// @Override
				// public void onAnimationRepeat(Animation animation) {
				// }
				//
				// @Override
				// public void onAnimationStart(Animation animation) {
				// }
				//
				// @Override
				// public void onAnimationEnd(Animation animation) {
				// Intent intent = new Intent(MainActivity.this,
				// SearchActivity.class);
				// startActivityForResult(intent, 500);
				// overridePendingTransition(R.anim.animationb,
				// R.anim.animationa);
				// }
				// });
				// mFirstBg.startAnimation(animation);
				//
				// Intent intent = new Intent(MainActivity.this,
				// SearchActivity.class);
				// startActivityForResult(intent, 500);
				// overridePendingTransition(R.anim.animationb,
				// R.anim.animationa);
			}
		});
	}

	private void initData() {
		mGoodsAdapter = new GoodsAdapter(mContext, mGoodsList);
		mRightLv.setAdapter(mGoodsAdapter);
		mGoodsAdapter.notifyDataSetChanged();
		mRightLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(MainActivity.this,
						GoodsDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_KEY,
						mGoodsList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
				// getActivity().overridePendingTransition(R.anim.push_left_in,
				// R.anim.push_left_out);
			}
		});

		mCategoryAdapter = new CategoryAdapter(mContext, mCategoryList);
		mLeftLv.setAdapter(mCategoryAdapter);
		mCategoryAdapter.notifyDataSetChanged();
		mCategoryAdapter.setmCurrentSelect(1);
		mLeftLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mCategoryList.get(position).getPpid().equals("t_1")
						|| mCategoryList.get(position).getPpid().equals("t_0")) {

				} else {
					mCategoryAdapter.setmCurrentSelect(position);
					mCategoryAdapter.notifyDataSetChanged();

					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) mMsgMap
							.get(mCategoryList.get(position).getPpid()));
					refreshGoods();
					// mGoodsAdapter.notifyDataSetChanged();
				}
			}
		});

		// GoodsLogic.getCategroyList(mContext, mHandler, "");
		// GoodsLogic.getAllGoods(mContext, mHandler);

		GoodsLogic.getCategroyAndGoodsList(mContext, mHandler);
	}

	@Override
	protected void onResume() {
		refreshGoods();
		super.onResume();
	}

	private void refreshGoods() {
		for (int i = 0; i < CartManager.sCartList.size(); i++) {
			for (int j = 0; j < mGoodsList.size(); j++) {
				if (mGoodsList.get(j).getId()
						.equals(CartManager.sCartList.get(i).getId())) {
					mGoodsList.get(j).setNum(
							CartManager.sCartList.get(i).getNum());
				} else {
					mGoodsList.get(j).setNum("0");
				}
			}
		}
		mGoodsAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TranslateAnimation animation = new TranslateAnimation(0, 0, -y, 0);
		animation.setDuration(500);
		animation.setFillAfter(true);
		mFirstBg.startAnimation(animation);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
	}

}
