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
import android.widget.TextView;

public class MainGoodsAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	public MainGoodsAdapter(Context context, ArrayList<Goods> datas) {
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
			convertView = mInflater.inflate(R.layout.main_hori_goods_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.main_hori_goods_name_tv);
//			holder.mPrice = (TextView) convertView
//					.findViewById(R.id.goods_price_tv);
//			holder.mOriginalPrice = (TextView) convertView
//					.findViewById(R.id.goods_original_prices_tv);

			holder.mIcon = (ImageView) convertView.findViewById(R.id.main_hori_goods_icon_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mName.setText(mDatas.get(position).getName());
//		holder.mPrice.setText("￥" + mDatas.get(position).getFinalPrice());
//		holder.mOriginalPrice.setText("原价￥" + mDatas.get(position).getPrice());

		ImageLoader
				.getInstance()
				.displayImage(
						"http://i2.cqnews.net/fashion/attachement/jpg/site82/20130609/002522274bb0131e71bc43.jpg",
						holder.mIcon);

		return convertView;
	}

	static class ViewHolder {

		public TextView mName;

		public TextView mPrice;

		public TextView mOriginalPrice;

		public ImageView mIcon;
	}

}