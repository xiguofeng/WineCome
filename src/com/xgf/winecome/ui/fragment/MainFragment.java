package com.xgf.winecome.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Category;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.GoodsDetailActivity;
import com.xgf.winecome.ui.activity.SearchActivity;
import com.xgf.winecome.ui.adapter.CategoryAdapter;
import com.xgf.winecome.ui.adapter.GoodsAdapter;
import com.xgf.winecome.ui.view.listview.AnimatedExpandableListView;
import com.xgf.winecome.ui.view.listview.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.xgf.winecome.utils.Watcher;

public class MainFragment extends Fragment implements Watcher {

	private Context mContext;
	private LinearLayout mSearchLl;
	private AnimatedExpandableListView mLeftExpLv;
	private ListView mLeftLv;
	private ListView mRightLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private GoodsAdapter mGoodsAdapter;
	private ArrayList<Category> mCategoryList = new ArrayList<Category>();
	private CategoryAdapter mCategoryAdapter;
	private ExampleAdapter adapter;

	private LinearLayout mFirstBg;
	private View mRootView;
	private float y;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != mRootView) {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (null != parent) {
				parent.removeView(mRootView);
			}
		} else {
			mRootView = inflater.inflate(R.layout.main_fragment, null);
			initView(mRootView);
			initData2();
		}

		return mRootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initView(View view) {
		mContext = getActivity();
		// mLeftExpLv = (AnimatedExpandableListView)
		// view.findViewById(R.id.exp_lv);
		mLeftLv = (ListView) view.findViewById(R.id.home_left_lv);
		mRightLv = (ListView) view.findViewById(R.id.home_right_lv);

		mFirstBg = (LinearLayout) view.findViewById(R.id.home_bg);

		mSearchLl = (LinearLayout) view.findViewById(R.id.home_search_rl);
		mSearchLl.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// y = mSearchLl.getY();
				// TranslateAnimation animation = new TranslateAnimation(0, 0,
				// 0,
				// -y);
				// animation.setDuration(500);
				// animation.setFillAfter(true);
				// animation.setAnimationListener(new AnimationListener() {
				// @Override
				// public void onAnimationRepeat(Animation animation) {
				// }
				//
				// @Override
				// public void onAnimationStart(Animation animation) {
				// }
				//
				// @Override
				// public void onAnimationEnd(Animation animation) {
				// Intent intent = new Intent(getActivity(),
				// SearchActivity.class);
				// startActivityForResult(intent, 500);
				// getActivity().overridePendingTransition(
				// R.anim.animationb, R.anim.animationa);
				// }
				// });
				// mFirstBg.startAnimation(animation);

				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivityForResult(intent, 500);
				getActivity().overridePendingTransition(R.anim.animationb,
						R.anim.animationa);
			}
		});
	}

	private void initData() {
		List<GroupItem> items = new ArrayList<GroupItem>();

		// Populate our list with groups and it's children
		for (int i = 1; i < 100; i++) {
			GroupItem item = new GroupItem();
			item.title = "Group " + i;

			for (int j = 0; j < i; j++) {
				ChildItem child = new ChildItem();
				child.title = "Awesome item " + j;
				child.hint = "Too awesome";

				item.items.add(child);
			}

			items.add(item);
		}

		adapter = new ExampleAdapter(mContext);
		adapter.setData(items);

		mLeftExpLv.setAdapter(adapter);

		// In order to show animations, we need to use a custom click handler
		// for our ExpandableListView.
		mLeftExpLv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.
				if (mLeftExpLv.isGroupExpanded(groupPosition)) {
					mLeftExpLv.collapseGroupWithAnimation(groupPosition);
				} else {
					mLeftExpLv.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}

		});

	}

	private void initData2() {
		// TODO 假数据
		for (int i = 0; i < 20; i++) {
			Goods goods = new Goods();
			goods.setId("" + i);
			goods.setName("酒" + i);
			goods.setPrice("" + i);
			goods.setNum("1");
			mGoodsList.add(goods);

			Category category = new Category();

			if (i > 0 && i < 6) {
				category.setName("白酒" + i);
				category.setLevel("1");
			} else if (i > 6 && i < 14) {
				category.setName("红酒" + i);
				category.setLevel("1");
			} else if (i > 14) {
				category.setName("葡萄酒" + i);
				category.setLevel("1");
			}

			if (0 == i) {
				category.setName("白酒");
				category.setLevel("0");
			} else if (6 == i) {
				category.setName("红酒");
				category.setLevel("0");
			} else if (14 == i) {
				category.setName("葡萄酒");
				category.setLevel("0");
			}
			mCategoryList.add(category);
		}
		mGoodsAdapter = new GoodsAdapter(mContext, mGoodsList);
		mRightLv.setAdapter(mGoodsAdapter);
		mGoodsAdapter.notifyDataSetChanged();
		mRightLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.e("xxx_mRightLv_onItemClick", "1");
				Intent intent = new Intent(getActivity(),
						GoodsDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_KEY,
						mGoodsList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
				// getActivity().overridePendingTransition(R.anim.push_left_in,
				// R.anim.push_left_out);
			}
		});

		mCategoryAdapter = new CategoryAdapter(mContext, mCategoryList);
		mLeftLv.setAdapter(mCategoryAdapter);
		mCategoryAdapter.notifyDataSetChanged();
	}

	private static class GroupItem {
		String title;
		List<ChildItem> items = new ArrayList<ChildItem>();
	}

	private static class ChildItem {
		String title;
		String hint;
	}

	private static class ChildHolder {
		TextView title;
		TextView hint;
	}

	private static class GroupHolder {
		TextView title;
	}

	/**
	 * Adapter for our list of {@link GroupItem}s.
	 */
	private class ExampleAdapter extends AnimatedExpandableListAdapter {
		private LayoutInflater inflater;

		private List<GroupItem> items;

		public ExampleAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<GroupItem> items) {
			this.items = items;
		}

		@Override
		public ChildItem getChild(int groupPosition, int childPosition) {
			return items.get(groupPosition).items.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getRealChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildHolder holder;
			ChildItem item = getChild(groupPosition, childPosition);
			if (convertView == null) {
				holder = new ChildHolder();
				convertView = inflater.inflate(R.layout.list_children_item,
						parent, false);
				holder.title = (TextView) convertView
						.findViewById(R.id.textTitle);
				holder.hint = (TextView) convertView
						.findViewById(R.id.textHint);
				convertView.setTag(holder);
			} else {
				holder = (ChildHolder) convertView.getTag();
			}

			holder.title.setText(item.title);
			holder.hint.setText(item.hint);

			return convertView;
		}

		@Override
		public int getRealChildrenCount(int groupPosition) {
			return items.get(groupPosition).items.size();
		}

		@Override
		public GroupItem getGroup(int groupPosition) {
			return items.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return items.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder holder;
			GroupItem item = getGroup(groupPosition);
			if (convertView == null) {
				holder = new GroupHolder();
				convertView = inflater.inflate(R.layout.list_group_item,
						parent, false);
				holder.title = (TextView) convertView
						.findViewById(R.id.textTitle);
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}

			holder.title.setText(item.title);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

	}

	@Override
	public void update(String str) {
	}

}