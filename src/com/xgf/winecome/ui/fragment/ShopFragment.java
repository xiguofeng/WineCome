package com.xgf.winecome.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xgf.winecome.R;

public class ShopFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 首先接收新建Fragment时候传过来的参数
		Bundle args = getArguments();
		int layoutId = args.getInt("layoutId");
		int imageId = args.getInt("image");
		View view = inflater.inflate(layoutId, container, false);

		ImageView image = (ImageView) view.findViewById(R.id.image);
		image.setImageResource(imageId);
		return view;
	}

}