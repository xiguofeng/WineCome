package com.xgf.winecome.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;

import com.xgf.winecome.R;

public class CustomProgressDialog extends Dialog {

	Context context;

	public CustomProgressDialog(Context context) {
		super(context, R.style.DialogTheme);
		this.context = context;
		this.setCanceledOnTouchOutside(false);
		init();
	}

	void init() {
		setContentView(R.layout.custom_dialog);
	}

}
