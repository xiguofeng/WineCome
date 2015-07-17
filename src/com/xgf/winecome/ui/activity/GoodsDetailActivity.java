package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.utils.CartManager;

public class GoodsDetailActivity extends Activity implements OnClickListener {

	public static final String GOODS_KEY = "GoodsKey";

	private TextView mGoodsNameTv;

	private TextView mGoodsPriceTv;

	private TextView mGoodsOrgPriceTv;

	private TextView mGoodsProductAreaTv;

	private TextView mGoodsDegreeTv;

	private TextView mGoodsNumTypeTv;

	private TextView mGoodsScentTv;

	private ImageView mGoodsIconIv;

	private ImageView mBackIv;

	private ImageView mCartIv;

	private LinearLayout mAddCartLl;

	private LinearLayout mNowBuyLl;

	private Goods mGoods;

	private String mGoodsId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_detail);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		mGoodsIconIv = (ImageView) findViewById(R.id.goods_detail_iv);

		mGoodsNameTv = (TextView) findViewById(R.id.goods_detail_name_tv);
		mGoodsPriceTv = (TextView) findViewById(R.id.goods_detail_price_tv);
		mGoodsOrgPriceTv = (TextView) findViewById(R.id.goods_detail_original_prices_tv);

		mGoodsProductAreaTv = (TextView) findViewById(R.id.goods_detail_area_tv);
		mGoodsDegreeTv = (TextView) findViewById(R.id.goods_detail_degree_tv);
		mGoodsNumTypeTv = (TextView) findViewById(R.id.goods_detail_num_type_tv);
		mGoodsScentTv = (TextView) findViewById(R.id.goods_detail_scent_tv);

		mBackIv = (ImageView) findViewById(R.id.goods_detail_back_iv);
		mBackIv.setOnClickListener(this);
		mCartIv = (ImageView) findViewById(R.id.goods_detail_cart_iv);
		mCartIv.setOnClickListener(this);

		mAddCartLl = (LinearLayout) findViewById(R.id.goods_detail_add_cart_ll);
		mAddCartLl.setOnClickListener(this);
		mNowBuyLl = (LinearLayout) findViewById(R.id.goods_detail_now_buy_ll);
		mNowBuyLl.setOnClickListener(this);
	}

	private void initData() {
		mGoods = (Goods) getIntent().getSerializableExtra(
				GoodsDetailActivity.GOODS_KEY);
		if (null != mGoods) {
			fillUpGoodsData();
		}
	}

	private void fillUpGoodsData() {
		mGoodsIconIv.setBackgroundResource(R.drawable.red_wine);

		mGoodsNameTv.setText(!TextUtils.isEmpty(mGoods.getName()) ? mGoods
				.getName() : "");
		mGoodsPriceTv.setText(!TextUtils.isEmpty(mGoods.getPrice()) ? mGoods
				.getPrice() : "");
		mGoodsOrgPriceTv
				.setText(!TextUtils.isEmpty(mGoods.getOrginPrice()) ? mGoods
						.getOrginPrice() : "");
		mGoodsProductAreaTv
				.setText(!TextUtils.isEmpty(mGoods.getProductArea()) ? mGoods
						.getProductArea() : "");
		mGoodsDegreeTv.setText(!TextUtils.isEmpty(mGoods.getDegree()) ? mGoods
				.getDegree() : "");
		mGoodsNumTypeTv
				.setText(!TextUtils.isEmpty(mGoods.getNumType()) ? mGoods
						.getNumType() : "");
		mGoodsScentTv.setText(!TextUtils.isEmpty(mGoods.getScent()) ? mGoods
				.getScent() : "");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_detail_cart_iv:
		case R.id.goods_detail_add_cart_ll: {
			CartManager.cartModifyByDetail(mGoods);
			break;
		}
		case R.id.goods_detail_now_buy_ll: {
			break;
		}
		case R.id.goods_detail_back_iv: {
			finish();
			// overridePendingTransition(R.anim.push_right_in,
			// R.anim.push_right_out);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
}
