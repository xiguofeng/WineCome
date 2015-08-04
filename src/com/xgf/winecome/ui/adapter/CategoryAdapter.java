package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Category;

public class CategoryAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Category> mDatas;

	private LayoutInflater mInflater;

	private int mCurrentSelect;

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

	@SuppressLint("NewApi")
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
			holder.mSelectIv = (ImageView) convertView
					.findViewById(R.id.category_item_select_iv);
			holder.mIconIv = (ImageView) convertView
					.findViewById(R.id.category_item_icon_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mSelectIv.setVisibility(View.INVISIBLE);
		holder.mIconIv.setVisibility(View.INVISIBLE);
		if ("t_0".equals(mDatas.get(position).getPpid())) {
			
			holder.mIconIv.setVisibility(View.VISIBLE);
			holder.mIconIv.setBackground(mContext.getResources().getDrawable(
					R.drawable.white_wine));
			holder.mSelectIv.setVisibility(View.VISIBLE);
		}

		if ("t_1".equals(mDatas.get(position).getPpid())) {
			// holder.mBg.setBackgroundColor(mContext.getResources().getColor(
			// R.color.white));
			holder.mIconIv.setVisibility(View.VISIBLE);
			holder.mIconIv.setBackground(mContext.getResources().getDrawable(
					R.drawable.white_wine));
			holder.mSelectIv.setVisibility(View.VISIBLE);
		}
		
		if (position == mCurrentSelect) {
			holder.mBg.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));
		}
		holder.mName.setText(mDatas.get(position).getPpmc());
		return convertView;
	}

	static class ViewHolder {

		public LinearLayout mBg;

		public TextView mName;

		public ImageView mSelectIv;

		public ImageView mIconIv;
	}

	public int getmCurrentSelect() {
		return mCurrentSelect;
	}

	public void setmCurrentSelect(int mCurrentSelect) {
		this.mCurrentSelect = mCurrentSelect;
	}
}
