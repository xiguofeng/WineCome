package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.PromotionNew;
import com.xgf.winecome.ui.activity.PromotionActivity;

public class MainBannerAdapter extends BaseAdapter {

	private Context mContext;

	private LayoutInflater mInflater;

	private ArrayList<PromotionNew> mDatas;

	public MainBannerAdapter(Context context, ArrayList<PromotionNew> datas) {
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

	@SuppressLint("NewApi")
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

		int tempPosition = position % 3;
		if (0 == position % 3) {
			holder.mBannerIcon.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.banner1));
		} else if (1 == position % 3) {
			holder.mBannerIcon.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.banner2));
		} else {
			holder.mBannerIcon.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.banner3));
		}
		//
		// if (!TextUtils.isEmpty(mDatas.get(tempPosition).getImgUrl())) {
		// ImageLoader.getInstance().displayImage(mDatas.get(tempPosition).getImgUrl(),
		// holder.mBannerIcon);
		// }
		// ImageLoader.getInstance().displayImage(
		// mDatas.get(position % 3).getIconUrl(), holder.mBannerIcon);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = position % mDatas.size();
				if (null != mDatas.get(index).getPromotionId()
						&& !"".equals(mDatas.get(index).getPromotionId())) {

					// 在这里可以设置跳转界面
					Intent intent = new Intent(mContext,
							PromotionActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("jumpToUrl", mDatas.get(index)
							.getPromotionId());
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
