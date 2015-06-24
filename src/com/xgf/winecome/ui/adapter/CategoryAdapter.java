package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Category;

public class CategoryAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Category> mDatas;

	private LayoutInflater mInflater;

	public CategoryAdapter(Context context, ArrayList<Category> datas) {
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
			convertView = mInflater.inflate(R.layout.category_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.category_item_name_tv);
			holder.mBg = (LinearLayout) convertView
					.findViewById(R.id.category_item_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mBg.setBackgroundColor(mContext.getResources().getColor(
				R.color.white));
		if ("0".equals(mDatas.get(position).getLevel())) {
			holder.mBg.setBackgroundColor(mContext.getResources().getColor(
					R.color.blue_content_bg));
		} 

		holder.mName.setText(mDatas.get(position).getName());
		return convertView;
	}

	static class ViewHolder {

		public LinearLayout mBg;
		public TextView mName;

	}
}
