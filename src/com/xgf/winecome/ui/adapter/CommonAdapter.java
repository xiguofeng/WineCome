package com.xgf.winecome.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgf.winecome.R;

public class CommonAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;

	public CommonAdapter(Context context,List<Map<String, Object>> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.myinfo_list_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.textInfo = (TextView) convertView
					.findViewById(R.id.textInfo);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.img.setBackgroundResource((Integer) mData.get(position).get(
				"img"));
		holder.textInfo.setText((String) mData.get(position).get("textInfo"));
		return convertView;
	}
	
	public final class ViewHolder{
		public ImageView img;
		public TextView textInfo;
	}
	
	public List<Map<String, Object>> getmData() {
		return mData;
	}

	public void setmData(List<Map<String, Object>> mData) {
		this.mData = mData;
	}

}