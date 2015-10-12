package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Category;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.Promotion;
import com.xgf.winecome.network.logic.AppLogic;
import com.xgf.winecome.network.logic.GoodsLogic;
import com.xgf.winecome.ui.adapter.BannerAdapter;
import com.xgf.winecome.ui.adapter.CategoryAdapter;
import com.xgf.winecome.ui.adapter.GoodsAdapter;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.ui.view.viewflow.CircleFlowIndicator;
import com.xgf.winecome.ui.view.viewflow.ViewFlow;
import com.xgf.winecome.utils.CartManager;
import com.xgf.winecome.utils.SystemUtils;

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;
	private LinearLayout mMainLl;
	private LinearLayout mSearchLl;
	private EditText mSearchEt;
	private ImageView mSearchIv;
	private ListView mLeftLv;
	private ListView mRightLv;
	private static ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private static GoodsAdapter mGoodsAdapter;
	private ArrayList<Category> mCategoryList = new ArrayList<Category>();
	private CategoryAdapter mCategoryAdapter;

	private ArrayList<Goods> mTempCategoryGoodsList = new ArrayList<Goods>();

	private float y;
	private HashMap<String, Object> mAllMsgMap = new HashMap<String, Object>();
	private HashMap<String, Object> mSearchMsgMap = new HashMap<String, Object>();
	private HashMap<String, Object> mShowMsgMap = new HashMap<String, Object>();

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mIndic;
	private ArrayList<Promotion> mBannerActivityList = new ArrayList<Promotion>();
	private BannerAdapter mBannerAdapter;
	private FrameLayout mBannerFl;
	protected CustomProgressDialog2 mCustomProgressDialog;

	private long exitTime = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case GoodsLogic.CATEGROY_GOODS_LIST_GET_SUC: {
				if (null != msg.obj) {
					mAllMsgMap.clear();
					mAllMsgMap
							.putAll((Map<? extends String, ? extends Object>) msg.obj);

					firstShowData(mAllMsgMap);
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
			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.dismiss();
			}
		}

	};

	Handler mBannerHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case GoodsLogic.PROMOTION_GET_SUC: {
				if (null != msg.obj) {

					ArrayList<Promotion> promotionslArrayList = new ArrayList<Promotion>();
					promotionslArrayList
							.addAll((Collection<? extends Promotion>) msg.obj);

					if (promotionslArrayList.size() > 0) {
						mBannerActivityList.clear();

						if (mBannerActivityList.size() > 3) {
							for (int i = 0; i < 3; i++) {
								mBannerActivityList.add(promotionslArrayList
										.get(i));
							}
						} else {
							mBannerActivityList.addAll(promotionslArrayList);
						}

						initCircleimage();
					}
				}
				break;
			}
			case GoodsLogic.PROMOTION_GET_FAIL: {
				break;
			}
			case GoodsLogic.PROMOTION_GET_EXCEPTION: {
				break;
			}
			case GoodsLogic.NET_ERROR: {
				break;
			}
			default:
				break;
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
		mLeftLv = (ListView) findViewById(R.id.main_left_lv);
		mRightLv = (ListView) findViewById(R.id.main_right_lv);

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
		mRightLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mRightLv.setFocusable(true);
				mRightLv.setFocusableInTouchMode(true);
				mRightLv.requestFocus();
				return false;
			}
		});
		mLeftLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mLeftLv.setFocusable(true);
				mLeftLv.setFocusableInTouchMode(true);
				mLeftLv.requestFocus();
				return false;
			}
		});

		mSearchLl.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

			}
		});
	}

	private void initCircleimage() {
		mBannerFl = (FrameLayout) findViewById(R.id.main_framelayout);
		mBannerFl.setVisibility(View.VISIBLE);
		mViewFlow = (ViewFlow) findViewById(R.id.main_viewflow);
		mIndic = (CircleFlowIndicator) findViewById(R.id.main_viewflowindic);
		// for(int i=0;i<3;i++){
		// Promotion promotion = new Promotion();
		// promotion.setImgUrl("");
		// mBannerActivityList.add(promotion);
		// }

		showcircleimage();
	}

	private void showcircleimage() {
		mBannerAdapter = new BannerAdapter(mContext, mBannerActivityList);
		mViewFlow.setAdapter(mBannerAdapter);
		mViewFlow.setmSideBuffer(3); // 实际图片张数
		mViewFlow.setFlowIndicator(mIndic);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(3 * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mViewFlow.requestFocus();
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
		mLeftLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCategoryAdapter.setmCurrentSelect(mCategoryList.get(position)
						.getPpid());
				String nowPPid = mCategoryList.get(position).getPpid();
				mCategoryAdapter.notifyDataSetChanged();
				if ("true".equals(mCategoryList.get(position)
						.getIsTopCategory())) {

					refreshAllDataByOnItemClick(mAllMsgMap);
					mTempCategoryGoodsList.clear();
					for (int i = 0; i < mCategoryList.size(); i++) {
						if (mCategoryList.get(i).getPplx().equals(nowPPid)) {
							mTempCategoryGoodsList
									.addAll((Collection<? extends Goods>) mShowMsgMap
											.get(mCategoryList.get(i).getPpid()));
						}
					}
					mGoodsList.clear();
					mGoodsList.addAll(mTempCategoryGoodsList);

				} else {
					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) mShowMsgMap
							.get(mCategoryList.get(position).getPpid()));

				}
				refreshGoods();
			}
		});

		// GoodsLogic.getCategroyList(mContext, mHandler, "");
		// GoodsLogic.getAllGoods(mContext, mHandler);

		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		GoodsLogic.promotionPage(mContext, mBannerHandler);
		GoodsLogic.getAllGoodsList(mContext, mHandler);

	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshGoods();
	}

	private static void refreshGoods() {
		for (int i = 0; i < mGoodsList.size(); i++) {
			boolean isHas = false;
			for (int j = 0; j < CartManager.getsCartList().size(); j++) {
				if (mGoodsList.get(i).getId()
						.equals(CartManager.getsCartList().get(j).getId())) {
					mGoodsList.get(i).setNum(
							CartManager.getsCartList().get(j).getNum());
					isHas = true;
				}
			}
			if (!isHas) {
				mGoodsList.get(i).setNum("0");
			}
		}
		mGoodsAdapter.notifyDataSetChanged();
		if (mGoodsList.size() > 0) {
			CartManager.setTotalMoney(true);
		}
	}

	private void search(String key) {
		mSearchMsgMap.clear();
		ArrayList<Category> categoryList = new ArrayList<Category>();
		categoryList.addAll((Collection<? extends Category>) mAllMsgMap
				.get("Category"));
		// ArrayList<Category> tempCategoryList = new ArrayList<Category>();
		ArrayList<Category> tempCategoryList = new ArrayList<Category>();
		for (int i = 0; i < categoryList.size(); i++) {
			ArrayList<Goods> arrayList = new ArrayList<Goods>();
			if (!"true".equals(categoryList.get(i).getIsTopCategory())) {
				arrayList.addAll((((Collection<? extends Goods>) mAllMsgMap
						.get(categoryList.get(i).getPpid()))));
				ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
				for (int j = 0; j < arrayList.size(); j++) {
					Goods goods = arrayList.get(j);
					if (goods.getName().contains(key)) {
						tempGoodsList.add(goods);
					}
				}
				if (tempGoodsList.size() > 0) {
					tempCategoryList.add(categoryList.get(i));
					mSearchMsgMap.put(categoryList.get(i).getPpid(),
							tempGoodsList);
				}
			} else {
				tempCategoryList.add(categoryList.get(i));
			}
		}

		ArrayList<Category> newCategoryList = new ArrayList<Category>();
		for (Category category : tempCategoryList) {
			newCategoryList.add(category);
		}

		mSearchMsgMap.put("Category", tempCategoryList);
		if (newCategoryList.size() > 1) {
			refresSearchData(mSearchMsgMap);
		}
	}

	private void firstShowData(HashMap<String, Object> msgMap) {
		refreshAllData(msgMap);
		mCategoryAdapter.setmCurrentSelect(mCategoryList.get(0).getPpid());
		mTempCategoryGoodsList.clear();
		for (int i = 0; i < mCategoryList.size(); i++) {
			if (mCategoryList.get(i).getPplx()
					.equals(mCategoryList.get(0).getPpid()))
				mTempCategoryGoodsList
						.addAll((Collection<? extends Goods>) mShowMsgMap
								.get(mCategoryList.get(i).getPpid()));

		}
		mGoodsList.clear();
		mGoodsList.addAll(mTempCategoryGoodsList);
		mGoodsAdapter.notifyDataSetChanged();
	}

	private void refreshAllData(HashMap<String, Object> msgMap) {
		mShowMsgMap.clear();
		mShowMsgMap.putAll(msgMap);

		mCategoryList.clear();
		mCategoryList.addAll((Collection<? extends Category>) mShowMsgMap
				.get("Category"));
		mCategoryAdapter.notifyDataSetChanged();

		mGoodsList.clear();
		mGoodsList.addAll((Collection<? extends Goods>) mShowMsgMap
				.get(mCategoryList.get(1).getPpid()));
		mGoodsAdapter.notifyDataSetChanged();
	}

	private void refreshAllDataByOnItemClick(HashMap<String, Object> msgMap) {
		mShowMsgMap.clear();
		mShowMsgMap.putAll(msgMap);

		mCategoryList.clear();
		mCategoryList.addAll((Collection<? extends Category>) mShowMsgMap
				.get("Category"));
		mCategoryAdapter.notifyDataSetChanged();

		mGoodsList.clear();
		mGoodsList.addAll((Collection<? extends Goods>) mShowMsgMap
				.get(mCategoryList.get(1).getPpid()));
		mGoodsAdapter.notifyDataSetChanged();
	}

	private void refresSearchData(HashMap<String, Object> msgMap) {
		mShowMsgMap.clear();
		mShowMsgMap.putAll(msgMap);

		mCategoryList.clear();
		mCategoryList.addAll((Collection<? extends Category>) mShowMsgMap
				.get("Category"));
		mCategoryAdapter.notifyDataSetChanged();

		mGoodsList.clear();
		int index = 1;
		for (int i = 0; i < mCategoryList.size(); i++) {
			if (!"true".equals(mCategoryList.get(i).getIsTopCategory())) {
				index = i;
				break;
			}
		}
		mCategoryAdapter.setmCurrentSelect(mCategoryList.get(index).getPpid());
		mGoodsList.addAll((Collection<? extends Goods>) mShowMsgMap
				.get(mCategoryList.get(index).getPpid()));
		mGoodsAdapter.notifyDataSetChanged();
	}

	private void checkUpdateVersion() {
		AppLogic.getVersion(mContext, mVersionHandler,
				SystemUtils.getVersion(getApplicationContext()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_search_iv: {
			if (!TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
				search(mSearchEt.getText().toString().trim());
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

	public static void update() {
		refreshGoods();
	}
}
