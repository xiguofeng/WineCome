package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpecialEventsGvAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	public SpecialEventsGvAdapter(Context context, ArrayList<Goods> datas) {
		this.mContext = context;
		this.mDatas = datas;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.special_events_gv_item, null);

			holder = new ViewHolder();
			holder.mGoodsIconIv = (ImageView) convertView.findViewById(R.id.special_events_gv_item_icon_iv);
			holder.mGoodsNameTv = (TextView) convertView.findViewById(R.id.special_events_gv_item_name_tv);
			holder.mGoodsMarketPriceTv = (TextView) convertView.findViewById(R.id.special_events_gv_goods_original_prices_tv);
			holder.mGoodsSalePriceTv = (TextView) convertView.findViewById(R.id.special_events_gv_goods_price_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(mDatas.get(position).getIconUrl(), holder.mGoodsIconIv);
		holder.mGoodsNameTv.setText(mDatas.get(position).getName());
		holder.mGoodsMarketPriceTv.setText("原价：￥"+mDatas.get(position).getMarketPrice());
		holder.mGoodsSalePriceTv.setText("促销价：￥"+mDatas.get(position).getSalesPrice());
		return convertView;
	}

	static class ViewHolder {

		public ImageView mGoodsIconIv;

		public TextView mGoodsNameTv;

		public TextView mGoodsMarketPriceTv;

		public TextView mGoodsSalePriceTv;

		private LinearLayout mExchangeLl;

	}

}
