package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.utils.CartManager;
import com.xgf.winecome.utils.Watched;
import com.xgf.winecome.utils.Watcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsAdapter extends BaseAdapter implements Watched {

	private Context mContext;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	private List<Watcher> mWatcherlist = new ArrayList<Watcher>();

	public GoodsAdapter(Context context, ArrayList<Goods> datas) {
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
			convertView = mInflater.inflate(R.layout.list_goods_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.goods_name_tv);
			holder.mPrice = (TextView) convertView.findViewById(R.id.goods_price_tv);
			holder.mOriginalPrice = (TextView) convertView.findViewById(R.id.goods_original_prices_tv);

			holder.mAddIb = (ImageButton) convertView.findViewById(R.id.goods_add_ib);
			holder.mReduceIb = (ImageButton) convertView.findViewById(R.id.goods_reduce_ib);

			holder.mNum = (EditText) convertView.findViewById(R.id.goods_count_et);
			holder.mIcon = (ImageView) convertView.findViewById(R.id.goods_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mName.setText(mDatas.get(position).getName());
		holder.mPrice.setText("￥" + mDatas.get(position).getSalesPrice());
		holder.mOriginalPrice.setText("原价￥" + mDatas.get(position).getMarketPrice());
		holder.mNum.setText(mDatas.get(position).getNum());

		ImageLoader.getInstance().displayImage(mDatas.get(position).getIconUrl(), holder.mIcon);

		final int tempPosition = position;
		holder.mAddIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Goods goods = mDatas.get(tempPosition);
				goods.setNum(String.valueOf(Integer.parseInt(goods.getNum()) + 1));
				mDatas.set(tempPosition, goods);
				CartManager.cartModifyByMain(goods);
				notifyDataSetChanged();

			}
		});
		holder.mReduceIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Goods goods = mDatas.get(tempPosition);
				if (Integer.parseInt(goods.getNum()) > 0) {
					goods.setNum(String.valueOf(Integer.parseInt(goods.getNum()) - 1));
					mDatas.set(tempPosition, goods);
					CartManager.cartModifyByMain(goods);
					notifyDataSetChanged();
				}

			}
		});
		return convertView;
	}

	static class ViewHolder {

		public TextView mName;

		public TextView mPrice;

		public TextView mOriginalPrice;

		public ImageView mIcon;

		public ImageButton mAddIb;

		public ImageButton mReduceIb;

		public EditText mNum;
	}

	@Override
	public void addWatcher(Watcher watcher) {
		mWatcherlist.add(watcher);
	}

	@Override
	public void removeWatcher(Watcher watcher) {

	}

	@Override
	public void notifyWatchers(String str) {

	}
}
