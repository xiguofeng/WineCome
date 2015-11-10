package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.PromotionNew;
import com.xgf.winecome.network.logic.AppLogic;
import com.xgf.winecome.network.logic.PromotionLogic;
import com.xgf.winecome.ui.adapter.MainBannerAdapter;
import com.xgf.winecome.ui.adapter.MainGoodsAdapter;
import com.xgf.winecome.ui.view.CustomClassifyView;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.ui.view.listview.HorizontalListView;
import com.xgf.winecome.ui.view.viewflow.CircleFlowIndicator;
import com.xgf.winecome.ui.view.viewflow.ViewFlow;
import com.xgf.winecome.utils.CartManager;

public class MainActivity extends Activity implements OnClickListener {

	public static final String RECOMMEND = "recommend";
	public static final String ACTIVITYAREA = "activityArea";
	public static final String BANNER = "board";

	private Context mContext;
	private LinearLayout mMainLl;
	private LinearLayout mSearchLl;
	private EditText mSearchEt;
	private ImageView mSearchIv;

	private ArrayList<PromotionNew> mPromotionList = new ArrayList<PromotionNew>();

	private ArrayList<PromotionNew> mRecommendPromotionList = new ArrayList<PromotionNew>();
	private ArrayList<PromotionNew> mActivityAreaPromotionList = new ArrayList<PromotionNew>();

	private HorizontalListView mHotGoodsLv;
	private ArrayList<Goods> mHotGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mHotGoodsAdapter;

	private LinearLayout mCategoryAndGoodsListLl;

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mIndic;
	private MainBannerAdapter mBannerAdapter;
	private ArrayList<PromotionNew> mBannerPromotionList = new ArrayList<PromotionNew>();
	private FrameLayout mBannerFl;
	protected CustomProgressDialog2 mCustomProgressDialog;

	private long exitTime = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case PromotionLogic.PROMOTION_ALL_LIST_GET_SUC: {
				if (null != msg.obj) {
					mPromotionList.clear();
					mPromotionList = (ArrayList<PromotionNew>) msg.obj;
					filterPromotion();
				}
				break;
			}
			case PromotionLogic.PROMOTION_ALL_LIST_GET_FAIL: {
				break;
			}
			case PromotionLogic.PROMOTION_ALL_LIST_GET_EXCEPTION: {
				break;
			}

			case PromotionLogic.NET_ERROR: {
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

	Handler mVersionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case AppLogic.GET_VERSION_SUC: {
				if (null != msg.obj) {
					String sDownUrl = (String) msg.obj;
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(sDownUrl));
					startActivity(intent);
				}
				break;
			}
			case AppLogic.GET_VERSION_FAIL: {
				break;
			}
			case AppLogic.GET_VERSION_EXCEPTION: {
				break;
			}
			case AppLogic.NET_ERROR: {
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
		mContext = MainActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		initView();
		initData();
	}

	private void initView() {
		mMainLl = (LinearLayout) findViewById(R.id.main_bg);
		mSearchLl = (LinearLayout) findViewById(R.id.main_search_ll);

		mSearchIv = (ImageView) findViewById(R.id.main_search_iv);
		mSearchIv.setOnClickListener(this);

		mSearchEt = (EditText) findViewById(R.id.main_search_et);
		mSearchEt
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// 此处为得到焦点时的处理内容
							mSearchLl.setVisibility(View.GONE);
							mSearchIv.setVisibility(View.VISIBLE);
						} else {
							// 此处为失去焦点时的处理内容
							mSearchEt.setText("");
							mSearchLl.setVisibility(View.VISIBLE);
							mSearchIv.setVisibility(View.GONE);
						}
					}
				});

		mMainLl.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				mMainLl.setFocusable(true);
				mMainLl.setFocusableInTouchMode(true);
				mMainLl.requestFocus();

				return false;
			}
		});

		mSearchLl.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

			}
		});
		initHLv();
		initGv();
		initCircleimage();
	}

	private void initHLv() {
		mHotGoodsLv = (HorizontalListView) findViewById(R.id.main_hot_goods_lv);
		mHotGoodsAdapter = new MainGoodsAdapter(mContext, mHotGoodsList);
		mHotGoodsLv.setAdapter(mHotGoodsAdapter);
	}

	private void initGv() {
		mCategoryAndGoodsListLl = (LinearLayout) findViewById(R.id.main_list_categoty_ll);
	}

	private void initCircleimage() {
		mBannerFl = (FrameLayout) findViewById(R.id.main_framelayout);
		mBannerFl.setVisibility(View.VISIBLE);
		mViewFlow = (ViewFlow) findViewById(R.id.main_viewflow);
		mIndic = (CircleFlowIndicator) findViewById(R.id.main_viewflowindic);
	}

	private void showcircleimage() {
		mBannerAdapter = new MainBannerAdapter(mContext, mBannerPromotionList);
		mViewFlow.setAdapter(mBannerAdapter);
		mViewFlow.setmSideBuffer(mBannerPromotionList.size()); // 实际图片张数
		mViewFlow.setFlowIndicator(mIndic);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(mBannerPromotionList.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mViewFlow.requestFocus();
	}

	private void initData() {
		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		PromotionLogic.getAllPromotion(mContext, mHandler);
	}

	private void filterPromotion() {
		mRecommendPromotionList.clear();
		mActivityAreaPromotionList.clear();
		mBannerPromotionList.clear();

		for (int i = 0; i < mPromotionList.size(); i++) {
			// mRecommendPromotionList.add(mPromotionList.get(i));
			mActivityAreaPromotionList.add(mPromotionList.get(i));
			mBannerPromotionList.add(mPromotionList.get(i));
			if (RECOMMEND.equals(mPromotionList.get(i).getDisplayPlace())) {

			} else if (ACTIVITYAREA.equals(mPromotionList.get(i).getDisplayPlace())) {
				mActivityAreaPromotionList.add(mPromotionList.get(i));
			} else if (BANNER.equals(mPromotionList.get(i).getDisplayPlace())) {
				mRecommendPromotionList.add(mPromotionList.get(i));
				//mBannerPromotionList.add(mPromotionList.get(i));
			}
		}

		fillUpData();
	}

	private void fillUpData() {
		fillUpBannerData();
		fillUpActivityAreaData();
		fillUpRecommendData();
	}

	private void fillUpRecommendData() {
		mHotGoodsList.clear();
		mHotGoodsList.addAll(mRecommendPromotionList.get(0).getGoodsList());
		mHotGoodsAdapter.notifyDataSetChanged();
	}

	private void fillUpActivityAreaData() {
		mCategoryAndGoodsListLl.removeAllViews();
		for (PromotionNew promotionNew : mActivityAreaPromotionList) {
			CustomClassifyView cv = new CustomClassifyView(mContext,
					promotionNew);
			mCategoryAndGoodsListLl.addView(cv);
		}
	}

	private void fillUpBannerData() {
		showcircleimage();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_search_iv: {
			if (!TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
			} else {
				Toast.makeText(mContext, getString(R.string.search_hint),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), R.string.exit,
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				CartManager.getsCartList().clear();
				CartManager.getsSelectCartList().clear();
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
