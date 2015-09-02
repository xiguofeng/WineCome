package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.entity.OrderState;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.pay.PayConstants;
import com.xgf.winecome.ui.utils.ListItemClickHelp;
import com.xgf.winecome.ui.view.OrderWineView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderWineAdapter extends BaseAdapter {

	private Context mContext;

	private HashMap<String, Object> mMap;

	private LayoutInflater mInflater;

	private ListItemClickHelp mCallback;

	public OrderWineAdapter(Context context, HashMap<String, Object> datas, ListItemClickHelp callback) {
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
			holder.mId = (TextView) convertView.findViewById(R.id.list_order_group_id_tv);
			holder.mTime = (TextView) convertView.findViewById(R.id.list_order_group_time_tv);
			holder.mState = (TextView) convertView.findViewById(R.id.list_order_group_state_tv);

			holder.mCancelOrCommentBtn = (Button) convertView.findViewById(R.id.list_order_group_comment_or_cancel_btn);
			holder.mViewBtn = (Button) convertView.findViewById(R.id.list_order_group_see_btn);
			holder.mPayBtn = (Button) convertView.findViewById(R.id.list_order_group_continue_pay_btn);
			holder.mWineLl = (LinearLayout) convertView.findViewById(R.id.list_order_group_wine_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).size() > position) {

			holder.mId.setText(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(position).getId());
			holder.mTime.setText(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(position).getOrderTime());

			int orderStateCode = Integer
					.parseInt(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(position).getOrderStatus());
			if (orderStateCode <= OrderState.state.length) {
				holder.mState.setText(OrderState.state[orderStateCode - 1]);
			}

			String orderStateCodeStr = String.valueOf(orderStateCode);
			String orderPayStateCodeStr = String
					.valueOf(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(position).getPayStatus());

			holder.mPayBtn.setVisibility(View.GONE);
			holder.mCancelOrCommentBtn.setVisibility(View.GONE);
			holder.mViewBtn.setVisibility(View.GONE);
			if (orderStateCode > 6) {
				holder.mPayBtn.setVisibility(View.GONE);
				holder.mCancelOrCommentBtn.setVisibility(View.GONE);
				holder.mViewBtn.setVisibility(View.GONE);
			} else {
				holder.mViewBtn.setVisibility(View.VISIBLE);
				if (orderStateCodeStr.equals(OrderState.ORDER_STATUS_ORDERED)
						|| orderStateCodeStr.equals(OrderState.ORDER_STATUS_GRABBED)) {
					holder.mCancelOrCommentBtn.setVisibility(View.VISIBLE);
					holder.mCancelOrCommentBtn.setText(mContext.getString(R.string.order_cancel));
				} else if (orderStateCodeStr.equals(OrderState.ORDER_STATUS_CONFIRMED)) {
					holder.mCancelOrCommentBtn.setVisibility(View.VISIBLE);
					holder.mCancelOrCommentBtn.setText(mContext.getString(R.string.add_comment));
				}

				if (OrderState.ORDER_STATUS_ORDERED.equals(orderStateCodeStr)
						&& PayConstants.PAY_STATUS_UNPAID.equals(orderPayStateCodeStr)) {
					holder.mPayBtn.setVisibility(View.VISIBLE);

					holder.mCancelOrCommentBtn.setVisibility(View.VISIBLE);
					holder.mCancelOrCommentBtn.setText(mContext.getString(R.string.order_cancel));

					holder.mViewBtn.setVisibility(View.GONE);
				}
			}

			final int tempPosition = position;
			final View view = convertView;
			final int whichCancel = holder.mCancelOrCommentBtn.getId();
			final int whichPay = holder.mPayBtn.getId();
			final int whichView = holder.mViewBtn.getId();

			holder.mPayBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mCallback.onClick(view, v, tempPosition, whichPay);

				}
			});

			holder.mCancelOrCommentBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mCallback.onClick(view, v, tempPosition, whichCancel);

				}
			});

			holder.mViewBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.onClick(view, v, tempPosition, whichView);
				}
			});

			holder.mWineLl.removeAllViews();

			ArrayList<Goods> goodsList = new ArrayList<Goods>();
			goodsList.addAll(((ArrayList<Goods>) mMap
					.get(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(position).getId())));
			for (int i = 0; i < goodsList.size(); i++) {
				// TODO
				Goods goods = goodsList.get(i);
				OrderWineView orderWineView = new OrderWineView(mContext, goods);
				holder.mWineLl.addView(orderWineView);
			}
		}
		return convertView;
	}

	static class ViewHolder {

		public TextView mState;

		public TextView mTime;

		public TextView mId;

		public Button mCancelOrCommentBtn;

		public Button mViewBtn;

		public Button mPayBtn;

		public LinearLayout mWineLl;

	}

}
