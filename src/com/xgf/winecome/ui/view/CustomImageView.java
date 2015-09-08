package com.xgf.winecome.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.winecome.R;
import com.xgf.winecome.utils.SystemUtils;

public class CustomImageView extends LinearLayout {

	private ImageView mIv;

	public CustomImageView(Context context, String url) {
		super(context);
		initView(context, url);
		fillData(context, url);
	}

	private void initView(final Context context, String url) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.custom_imageview, null);
		mIv = (ImageView) layout.findViewById(R.id.custom_iv);
		this.addView(layout);
	}

	public void fillData(Context context, String url) {
		int screenWidth = SystemUtils.getScreenWidth(context);
		ViewGroup.LayoutParams lp = mIv.getLayoutParams();
		lp.width = screenWidth;
		lp.height = LayoutParams.WRAP_CONTENT;
		mIv.setLayoutParams(lp);

		mIv.setMaxWidth(screenWidth);
		mIv.setMaxHeight(screenWidth * 3);
		ImageLoader.getInstance().displayImage(url, mIv);
	}

}
