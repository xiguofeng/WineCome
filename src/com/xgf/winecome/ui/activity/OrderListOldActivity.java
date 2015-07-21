package com.xgf.winecome.ui.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xgf.winecome.R;

public class OrderListOldActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ExpandableListView mOrderExlv;
	private List<String> parent = null;
	private Map<String, List<String>> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mContext = OrderListOldActivity.this;
		mOrderExlv = (ExpandableListView) findViewById(R.id.order_list_lv);
		mOrderExlv.setAdapter(new OrderExAdapter());
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
	}

	class OrderExAdapter extends BaseExpandableListAdapter {

		// 得到子item需要关联的数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			String key = parent.get(groupPosition);
			return (map.get(key).get(childPosition));
		}

		// 得到子item的ID
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 设置子item的组件
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String key = OrderListOldActivity.this.parent.get(groupPosition);
			String info = map.get(key).get(childPosition);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) OrderListOldActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_order_children_item, null);
			}
			TextView tv = (TextView) convertView
					.findViewById(R.id.list_order_group_num_tv);
			tv.setText(info);
			return tv;
		}

		// 获取当前父item下的子item的个数
		@Override
		public int getChildrenCount(int groupPosition) {
			String key = parent.get(groupPosition);
			int size = map.get(key).size();
			return size;
		}

		// 获取当前父item的数据
		@Override
		public Object getGroup(int groupPosition) {
			return parent.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parent.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 设置父item组件
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) OrderListOldActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_order_group_item, null);
			}
			TextView tv = (TextView) convertView
					.findViewById(R.id.list_order_group_num_tv);
			tv.setText(OrderListOldActivity.this.parent.get(groupPosition));
			return tv;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
