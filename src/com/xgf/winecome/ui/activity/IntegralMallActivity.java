package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.IntegralGoods;
import com.xgf.winecome.network.logic.IntegralGoodsLogic;
import com.xgf.winecome.ui.adapter.IntegralGoodsGvAdapter;
import com.xgf.winecome.ui.utils.ListItemClickHelp;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class IntegralMallActivity extends Activity implements OnClickListener, ListItemClickHelp {

	private Context mContext;

	private ImageView mBackIv;

	private GridView mIntegralGoodsGv;

	private RelativeLayout mFilterRl;

	private IntegralGoodsGvAdapter mGvAdapter;

	private ArrayList<IntegralGoods> mIntegralGoodsList = new ArrayList<IntegralGoods>();

	protected CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case IntegralGoodsLogic.INTEGRAL_GOODS_LIST_GET_SUC: {
				if (null != msg.obj) {
					mIntegralGoodsList.clear();
					mIntegralGoodsList.addAll((Collection<? extends IntegralGoods>) msg.obj);
					mGvAdapter.notifyDataSetChanged();
				}
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_GOODS_LIST_GET_FAIL: {
				break;
			}
			case IntegralGoodsLogic.INTEGRAL_GOODS_LIST_GET_EXCEPTION: {
				break;
			}


			default:
				break;
			}
			if (null != mCustomProgressDialog && mCustomProgressDialog.isShowing()) {
				mCustomProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_mall);
		mContext = IntegralMallActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mFilterRl = (RelativeLayout) findViewById(R.id.integral_mall_filter_rl);
		mIntegralGoodsGv = (GridView) findViewById(R.id.integral_mall_gv);
		mBackIv = (ImageView) findViewById(R.id.integral_mall_back_iv);

		mGvAdapter = new IntegralGoodsGvAdapter(IntegralMallActivity.this, mIntegralGoodsList, this);
		mIntegralGoodsGv.setAdapter(mGvAdapter);
	}

	private void setUpListener() {
		mBackIv.setOnClickListener(this);
		mFilterRl.setOnClickListener(this);
		mIntegralGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}

	private void setUpData() {
		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		IntegralGoodsLogic.getAllIntegralGoods(mContext, mHandler);

		mIntegralGoodsList.clear();
		// for (int i = 0; i < 10; i++) {
		// IntegralGoods integralGoods = new IntegralGoods();
		// integralGoods.setName("兑换商品" + i);
		// integralGoods.setIntegral("" + i + "积分");
		// mIntegralGoodsList.add(integralGoods);
		// }
		mGvAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.integral_mall_filter_rl: {
			Intent intent = new Intent(IntegralMallActivity.this, IntegralSelectActivity.class);
			startActivityForResult(intent, 1);
			break;
		}
		case R.id.integral_mall_back_iv: {
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
		case R.id.integral_gv_item_submit_btn: {
			// if (null != mCustomProgressDialog) {
			// mCustomProgressDialog.show();
			// }
			// IntegralGoodsLogic.exchange(mContext, mHandler,
			// UserInfoManager.getPhone(mContext),
			// mIntegralGoodsList.get(position).getId());

			Intent intent = new Intent(IntegralMallActivity.this, IntegralInfoInput.class);
			intent.putExtra("id", mIntegralGoodsList.get(position).getId());
			startActivity(intent);

			break;
		}
		default:
			break;
		}

	}
}
