package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.IntegralGoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IntegralOrderAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<IntegralGoods> mDatas;

	private LayoutInflater mInflater;

	public IntegralOrderAdapter(Context context, ArrayList<IntegralGoods> datas) {
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
			convertView = mInflater.inflate(R.layout.list_integral_order_item,
					null);

			holder = new ViewHolder();
			holder.mExchangeIntegralTv = (TextView) convertView
					.findViewById(R.id.list_integral_order_exchange_tv);
			holder.mExchangeNameTv = (TextView) convertView
					.findViewById(R.id.list_integral_order_name_tv);
			holder.mExchangeNumTv = (TextView) convertView
					.findViewById(R.id.list_integral_order_num_tv);
			holder.mExchangeDateTv = (TextView) convertView
					.findViewById(R.id.list_integral_order_date_tv);
			holder.mExchangeAddressTv = (TextView) convertView
					.findViewById(R.id.list_integral_order_address_tv);
			holder.mExchangePhoneTv = (TextView) convertView
					.findViewById(R.id.list_integral_order_phone_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mExchangeNameTv.setText(mDatas.get(position).getProductName());
		holder.mExchangeIntegralTv.setText(mDatas.get(position).getAmount());
		holder.mExchangeNumTv.setText(mDatas.get(position).getCount());
		holder.mExchangeDateTv.setText(mDatas.get(position).getOrderTime());
		holder.mExchangeAddressTv.setText(mDatas.get(position).getAddress());
		holder.mExchangePhoneTv.setText(mDatas.get(position).getPhone());
		return convertView;
	}

	static class ViewHolder {

		public TextView mExchangeIntegralTv;

		public TextView mExchangeNameTv;

		public TextView mExchangeNumTv;

		public TextView mExchangeDateTv;

		public TextView mExchangeAddressTv;

		public TextView mExchangePhoneTv;

	}

}
