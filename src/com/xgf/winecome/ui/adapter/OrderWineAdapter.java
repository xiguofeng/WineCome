package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.entity.OrderState;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.ui.utils.ListItemClickHelp;
import com.xgf.winecome.ui.view.OrderWineView;
import com.xgf.winecome.utils.Watched;
import com.xgf.winecome.utils.Watcher;

public class OrderWineAdapter extends BaseAdapter implements Watched {

	private Context mContext;

	private HashMap<String, Object> mMap;

	private LayoutInflater mInflater;

	private List<Watcher> mWatcherlist = new ArrayList<Watcher>();

	private ListItemClickHelp mCallback;

	public OrderWineAdapter(Context context, HashMap<String, Object> datas,
			ListItemClickHelp callback) {
		this.mContext = context;
		this.mMap = datas;
		this.mCallback = callback;
		mInflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		if (((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)) != null) {
			return ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).size();
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
			holder.mId = (TextView) convertView
					.findViewById(R.id.list_order_group_id_tv);
			holder.mTime = (TextView) convertView
					.findViewById(R.id.list_order_group_time_tv);
			holder.mState = (TextView) convertView
					.findViewById(R.id.list_order_group_state_tv);

			holder.mCancelBtn = (Button) convertView
					.findViewById(R.id.list_order_group_cancel_btn);
			holder.mDelOrViewBtn = (Button) convertView
					.findViewById(R.id.list_order_group_del_or_see_btn);
			holder.mWineLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_wine_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mId.setText(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
				.get(position).getId());
		holder.mTime.setText(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
				.get(position).getOrderTime());

		holder.mState.setText(OrderState.state[(Integer
				.parseInt(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
						.get(position).getOrderStatus()) - 1)]);

		final int tempPosition = position;
		final View view = convertView;
		final int whichCancel = holder.mCancelBtn.getId();
		final int whichDelOrView = holder.mDelOrViewBtn.getId();

		holder.mCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mCallback.onClick(view, v, tempPosition, whichCancel);

			}
		});
		holder.mDelOrViewBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onClick(view, v, tempPosition, whichDelOrView);
			}
		});

		holder.mWineLl.removeAllViews();

		ArrayList<Goods> goodsList = new ArrayList<Goods>();
		goodsList.addAll(((ArrayList<Goods>) mMap.get(((ArrayList<Order>) mMap
				.get(MsgResult.ORDER_TAG)).get(position).getId())));
		for (int i = 0; i < goodsList.size(); i++) {
			// TODO
			Goods goods = goodsList.get(i);
			OrderWineView orderWineView = new OrderWineView(mContext, goods);
			holder.mWineLl.addView(orderWineView);
		}
		return convertView;
	}

	static class ViewHolder {

		public TextView mState;

		public TextView mTime;

		public TextView mId;

		public Button mCancelBtn;

		public Button mDelOrViewBtn;

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
