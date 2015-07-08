package com.xgf.winecome.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xgf.winecome.R;
import com.xgf.winecome.ui.adapter.CommonAdapter;

public class MoreFragment extends Fragment implements OnItemClickListener {

	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (null != mRootView) {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (null != parent) {
				parent.removeView(mRootView);
			}
		} else {
			mRootView = inflater.inflate(R.layout.more_fragment, null);
			ListView listView = (ListView) mRootView
					.findViewById(R.id.listViewForMine);
			CommonAdapter adapter = new CommonAdapter(getActivity(), getData());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}

		return mRootView;
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map = new HashMap<String, Object>();
		map.put("textInfo", "历史订单");
		map.put("img", R.drawable.point_normal);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("textInfo", "积分商城");
		map.put("img", R.drawable.point_normal);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("textInfo", "设置");
		map.put("img", R.drawable.point_normal);
		list.add(map);
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			Intent intent = new Intent();

			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
		}

	}

}