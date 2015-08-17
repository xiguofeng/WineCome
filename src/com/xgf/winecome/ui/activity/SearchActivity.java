package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.adapter.GoodsAdapter;
import com.xgf.winecome.ui.view.AutoClearEditText;

public class SearchActivity extends Activity implements OnClickListener {

	private AutoClearEditText mSearchGoodsEt;

	private TextView mSearchTv;

	private String mSearchKey;

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private GoodsAdapter mGoodsAdapter;
	
	private ListView mGoodsLv;

	private final Context mContext = SearchActivity.this;

	// private long mExitTime = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		initView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	private void initView() {
		// 初始化控件
		mSearchGoodsEt = (AutoClearEditText) findViewById(R.id.search_et);
		mSearchTv = (TextView) findViewById(R.id.search_tv);
		mGoodsLv = (ListView) findViewById(R.id.search_goods_lv);
	}

	private void initData() {
		mSearchTv.setOnClickListener(this);
		mGoodsAdapter = new GoodsAdapter(mContext, mGoodsList);
		mGoodsLv.setAdapter(mGoodsAdapter);
		mGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
//				Intent intent = new Intent(mContext, OrderFormActivity.class);
//				Bundle bundle = new Bundle();
//				Goods goods = mGoodsList.get(position);
//				goods.setStandard("true");
//				bundle.putSerializable(OrderFormActivity.GOODS_KEY, goods);
//				intent.putExtras(bundle);
//				startActivity(intent);
//				overridePendingTransition(R.anim.push_left_in,
//						R.anim.push_left_out);

			}
		});

	}

	private void getGoodsData(String keyword) {
		// CacheManager.addSearchHistroy(getApplicationContext(), mSearchKey);
		// GoodsLogic.getGoodsByKey(mContext, mHandler, keyword,
		// MsgRequest.GOODS_PAGE_SIZE);

		// mGoodsList.clear();
		// for (int i = 0; i < 10; i++) {
		// Goods goods = new Goods();
		// goods.setName("可乐" + i);
		// goods.setPrice("" + i);
		// goods.setBrief("可口可乐出品" + i);
		// mGoodsList.add(goods);
		// }
		// mGoodsAdapter.notifyDataSetChanged();
	}

	protected void alertInfo() {
		showAlertDialog("查找信息", " 没有找到相关商品,继续查找！", "继续",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// Intent intent = new Intent(SearchActivity.this,
						// SpecialGoodsActivity.class);
						// intent.putExtra("goodsName", mSearchKey);
						// startActivity(intent);
						// overridePendingTransition(R.anim.push_left_in,
						// R.anim.push_left_out);
					}
				}, "取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	protected void showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_tv: {
			mSearchKey = mSearchGoodsEt.getText().toString().trim();
			if ("".equals(mSearchKey)) {
				Toast.makeText(
						mContext,
						mContext.getResources()
								.getString(R.string.search_thing),
						Toast.LENGTH_SHORT).show();
			} else {
				getGoodsData(mSearchKey);
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
			// if ((System.currentTimeMillis() - mExitTime) > 2000) {
			// Toast.makeText(getApplicationContext(), R.string.exit,
			// Toast.LENGTH_SHORT).show();
			// mExitTime = System.currentTimeMillis();
			// } else {
			// finish();
			// }
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
