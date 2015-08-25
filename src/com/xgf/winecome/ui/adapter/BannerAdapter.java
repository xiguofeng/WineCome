package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.SpecialEventsActivity;

public class BannerAdapter extends BaseAdapter {

	private Context mContext;

	private LayoutInflater mInflater;

	private ArrayList<Goods> mDatas;

	public BannerAdapter(Context context, ArrayList<Goods> datas) {
		mContext = context;
		this.mDatas = datas;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// 返回很大的值使得getView中的position不断增大来实现循环
		return Integer.MAX_VALUE;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.banner_item, null);

			holder = new ViewHolder();
			holder.mBannerIcon = (ImageView) convertView
					.findViewById(R.id.banner_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// ImageLoader.getInstance().displayImage(
		// mDatas.get(position % 3).getIconUrl(), holder.mBannerIcon);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = position % 3;
				if (null != mDatas.get(index).getId()
						&& !"".equals(mDatas.get(index).getId())) {

					// 在这里可以设置跳转界面
					Intent intent = new Intent(mContext,
							SpecialEventsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", mDatas.get(index).getId());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {

		public ImageView mBannerIcon;

	}

}
