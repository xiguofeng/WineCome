package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.view.OrderWineView;
import com.xgf.winecome.utils.OrderManager;
import com.xgf.winecome.utils.Watched;
import com.xgf.winecome.utils.Watcher;

public class OrderWineAdapter extends BaseAdapter implements Watched {

	private Context mContext;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	private List<Watcher> mWatcherlist = new ArrayList<Watcher>();

	public OrderWineAdapter(Context context, ArrayList<Goods> datas) {
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
			convertView = mInflater.inflate(R.layout.list_order_item, null);

			holder = new ViewHolder();
			holder.mState = (TextView) convertView
					.findViewById(R.id.list_order_group_num_tv);
			holder.mTime = (TextView) convertView
					.findViewById(R.id.list_order_group_time_tv);
			holder.mState = (TextView) convertView
					.findViewById(R.id.list_order_group_state_tv);

			holder.mCancelLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_cancel_ll);
			holder.mDelOrViewLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_del_or_see_ll);
			holder.mWineLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_wine_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mState.setText(mDatas.get(position).getStandard());
		holder.mTime.setText("￥" + mDatas.get(position).getPrice());
		holder.mState.setText("原价￥" + mDatas.get(position).getOrginPrice());

		final int tempPosition = position;
		holder.mCancelLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mDatas.remove(tempPosition);

			}
		});
		holder.mDelOrViewLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Goods goods = mDatas.get(tempPosition);
				goods.setNum(String.valueOf(Integer.parseInt(goods.getNum()) + 1));
				mDatas.set(tempPosition, goods);
				OrderManager.orderModify(goods);
				notifyDataSetChanged();

			}
		});

		//TODO
		OrderWineView orderWineView = new OrderWineView(mContext,
				mDatas.get(tempPosition));
		holder.mWineLl.addView(orderWineView);
		return convertView;
	}

	static class ViewHolder {

		public TextView mState;

		public TextView mTime;

		public TextView mNum;

		public LinearLayout mCancelLl;

		public LinearLayout mDelOrViewLl;

		public LinearLayout mWineLl;

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
