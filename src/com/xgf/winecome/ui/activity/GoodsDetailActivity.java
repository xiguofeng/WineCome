package com.xgf.winecome.ui.activity;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.utils.ActivitiyInfoManager;
import com.xgf.winecome.utils.CartManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsDetailActivity extends Activity implements OnClickListener {

	public static final String GOODS_KEY = "GoodsKey";

	private Context mContext;

	private TextView mGoodsNameTv;

	private TextView mGoodsPriceTv;

	private TextView mGoodsOrgPriceTv;

	private TextView mGoodsProductAreaTv;

	private TextView mGoodsFactoryTv;

	private TextView mGoodsBrandTv;

	private TextView mGoodsJHLTv;

	private TextView mGoodsDegreeTv;

	private TextView mGoodsScentTv;

	private TextView mGoodsMaterialTv;

	private TextView mGoodsBriefTv;

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
		mContext = GoodsDetailActivity.this;
		if (!ActivitiyInfoManager.activitityMap
				.containsKey(ActivitiyInfoManager
						.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap
					.put(ActivitiyInfoManager.getCurrentActivityName(mContext),
							this);
		}
		// AppManager.getInstance().addActivity(GoodsDetailActivity.this);
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
		mGoodsFactoryTv = (TextView) findViewById(R.id.goods_detail_factory_tv);
		mGoodsBrandTv = (TextView) findViewById(R.id.goods_detail_brand_type_tv);
		mGoodsJHLTv = (TextView) findViewById(R.id.goods_detail_jhl_tv);
		mGoodsDegreeTv = (TextView) findViewById(R.id.goods_detail_degree_tv);
		mGoodsScentTv = (TextView) findViewById(R.id.goods_detail_scent_tv);
		mGoodsMaterialTv = (TextView) findViewById(R.id.goods_detail_material_tv);
		mGoodsBriefTv = (TextView) findViewById(R.id.goods_detail_brief_tv);

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
		ImageLoader.getInstance().displayImage(mGoods.getIconUrl(),
				mGoodsIconIv);

		mGoods.setNum("1");
		mGoodsNameTv.setText(!TextUtils.isEmpty(mGoods.getName()) ? mGoods
				.getName() : "");
		mGoodsPriceTv.setText(!TextUtils.isEmpty(mGoods.getSalesPrice()) ? "¥"
				+ mGoods.getSalesPrice() : "¥");
		mGoodsOrgPriceTv
				.setText(!TextUtils.isEmpty(mGoods.getMarketPrice()) ? "原价:¥"
						+ mGoods.getMarketPrice() : "原价:¥");
		mGoodsProductAreaTv
				.setText(!TextUtils.isEmpty(mGoods.getArea()) ? "商品产地："
						+ mGoods.getArea() : "商品产地：");
		mGoodsFactoryTv.setText(!TextUtils.isEmpty(mGoods.getFactory()) ? "酒厂："
				+ mGoods.getFactory() : "酒厂：");
		mGoodsBrandTv.setText(!TextUtils.isEmpty(mGoods.getLevel()) ? "产品品牌："
				+ mGoods.getLevel() : "产品品牌：");
		mGoodsJHLTv.setText(!TextUtils.isEmpty(mGoods.getContent()) ? "净含量："
				+ mGoods.getContent() : "净含量：");
		mGoodsDegreeTv.setText(!TextUtils.isEmpty(mGoods.getDegree()) ? "酒精度："
				+ mGoods.getDegree() : "酒精度：");
		mGoodsScentTv.setText(!TextUtils.isEmpty(mGoods.getModel()) ? "香型："
				+ mGoods.getModel() : "香型：");
		mGoodsMaterialTv
				.setText(!TextUtils.isEmpty(mGoods.getMetrial()) ? "原料："
						+ mGoods.getMetrial() : "原料：");
		mGoodsBriefTv.setText(mGoods.getFactory());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_detail_cart_iv: {
			finish();
			HomeActivity.setTab(HomeActivity.TAB_CART);
			break;
		}
		case R.id.goods_detail_add_cart_ll: {
			boolean isSuc = CartManager.cartModifyByDetail(mGoods);
			Toast.makeText(getApplicationContext(),
					getString(R.string.add_cart_suc), Toast.LENGTH_SHORT)
					.show();
			break;
		}
		case R.id.goods_detail_now_buy_ll: {
			ArrayList<Goods> goodsList = new ArrayList<Goods>();
			goodsList.add(mGoods);
			CartManager.setsDetailBuyList(goodsList);

			Intent intent = new Intent(GoodsDetailActivity.this,
					PersonInfoActivity.class);
			intent.setAction(PersonInfoActivity.ORIGIN_FROM_DETAIL_ACTION);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
