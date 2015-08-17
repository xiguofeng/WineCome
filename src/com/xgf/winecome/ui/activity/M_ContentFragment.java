package com.xgf.winecome.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xgf.winecome.R;

public class M_ContentFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.m_fragment_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.fragment_tag);
		String tag = this.getArguments().getString("key");
		tv.setText("这是选择" + tag + "的内容");
		return view;
	}

}
