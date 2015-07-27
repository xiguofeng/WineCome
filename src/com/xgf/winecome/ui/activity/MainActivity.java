package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

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

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;
	private LinearLayout mSearchLl;
	private ListView mLeftLv;
	private ListView mRightLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private GoodsAdapter mGoodsAdapter;
	private ArrayList<Category> mCategoryList = new ArrayList<Category>();
	private CategoryAdapter mCategoryAdapter;

	private LinearLayout mFirstBg;
	private float y;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
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
				y = mSearchLl.getY();
				TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
						-y);
				animation.setDuration(500);
				animation.setFillAfter(true);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Intent intent = new Intent(MainActivity.this,
								SearchActivity.class);
						startActivityForResult(intent, 500);
						overridePendingTransition(R.anim.animationb,
								R.anim.animationa);
					}
				});
				mFirstBg.startAnimation(animation);

				Intent intent = new Intent(MainActivity.this,
						SearchActivity.class);
				startActivityForResult(intent, 500);
				overridePendingTransition(R.anim.animationb, R.anim.animationa);
			}
		});
	}

	private void initData() {

		//GoodsLogic.getCategroyList(mContext, mHandler, "白酒");
		GoodsLogic.getGoodsByCategroy(mContext, mHandler, "1", "1", "10");
		// TODO 假数据
		for (int i = 0; i < 20; i++) {
			Goods goods = new Goods();
			goods.setId("" + i);
			goods.setName("酒" + i);
			goods.setPrice("" + i);
			goods.setNum("1");
			mGoodsList.add(goods);

			Category category = new Category();

			if (i > 0 && i < 6) {
				category.setName("白酒" + i);
				category.setLevel("1");
			} else if (i > 6 && i < 14) {
				category.setName("红酒" + i);
				category.setLevel("1");
			} else if (i > 14) {
				category.setName("葡萄酒" + i);
				category.setLevel("1");
			}

			if (0 == i) {
				category.setName("白酒");
				category.setLevel("0");
			} else if (6 == i) {
				category.setName("红酒");
				category.setLevel("0");
			} else if (14 == i) {
				category.setName("葡萄酒");
				category.setLevel("0");
			}
			mCategoryList.add(category);
		}
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
