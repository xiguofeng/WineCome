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

public class MainGoodsGvAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Goods> mDatas;

	public MainGoodsGvAdapter(Context context, ArrayList<Goods> data) {

		this.context = context;
		this.mDatas = data;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {
		HolderView holderView = null;
		if (currentView == null) {
			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(
					R.layout.gv_main_goods_item, null);
			holderView.iconIv = (ImageView) currentView
					.findViewById(R.id.gv_main_goods_common_iv);
			holderView.nameTv = (TextView) currentView
					.findViewById(R.id.gv_main_goods_common_name_tv);

			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}

		// holderView.iconIv.setImageResource(data.get(position).getLocalImage());
		holderView.nameTv.setText(mDatas.get(position).getName());
		ImageLoader.getInstance().displayImage(mDatas.get(position).getIconUrl(),
				holderView.iconIv);
		return currentView;
	}

	public class HolderView {

		private ImageView iconIv;

		private TextView nameTv;

	}

}
