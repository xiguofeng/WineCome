package com.xgf.winecome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

import com.xgf.winecome.R;

public class InvoiceSelectActivity extends Activity implements OnClickListener {
	private RelativeLayout mWineRl;

	private RelativeLayout mFoodRl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invoice_select);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.9
		// p.alpha = 1.0f; // 设置本身透明度
		p.dimAmount = 0.6f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setAttributes(p);

		mWineRl = (RelativeLayout) findViewById(R.id.invoice_select_wine_rl);
		mFoodRl = (RelativeLayout) findViewById(R.id.invoice_select_food_rl);
	}

	private void setUpListener() {
		// 添加change事件
		// 添加onclick事件
		mWineRl.setOnClickListener(this);
		mFoodRl.setOnClickListener(this);
	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invoice_select_wine_rl: {
			Intent intent = new Intent();
			intent.putExtra("invoice_content_value", getString(R.string.wine));
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		case R.id.invoice_select_food_rl: {
			Intent intent = new Intent();
			intent.putExtra("invoice_content_value", getString(R.string.food));
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		default:
			break;
		}
	}
}
