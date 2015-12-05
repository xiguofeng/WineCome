package com.xgf.winecome.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import cn.jpush.android.api.JPushInterface;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Version;
import com.xgf.winecome.network.logic.AppLogic;
import com.xgf.winecome.ui.view.widget.AlertDialog;
import com.xgf.winecome.utils.SystemUtils;
import com.xgf.winecome.utils.UserInfoManager;

public class SplashActivity extends BaseActivity {

	public static final String TAG = SplashActivity.class.getSimpleName();

	private final int SPLISH_DISPLAY_LENGTH = 2000; // 延迟2秒启动登陆界面

	Handler mVersionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case AppLogic.GET_VERSION_SUC: {
				if (null != msg.obj) {
					Version version = (Version) msg.obj;
					final String downUrl = version.getUrl();
					if ("Y".equals(version.getForce())) {

						new AlertDialog(SplashActivity.this)
								.builder()
								.setTitle(getString(R.string.prompt))
								.setMsg("版本强制升级")
								.setPositiveButton("下载新版本",
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												Intent intent = new Intent();
												intent.setAction(Intent.ACTION_VIEW);
												intent.setData(Uri
														.parse(downUrl));
												startActivity(intent);
											}
										})
								.setNegativeButton("退出应用",
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												SplashActivity.this.finish();
											}
										}).show();

					}
				} else {
					jump();
				}
				break;
			}
			case AppLogic.GET_VERSION_FAIL: {
				jump();
				break;
			}
			case AppLogic.GET_VERSION_EXCEPTION: {
				jump();
				break;
			}
			case AppLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(SplashActivity.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(SplashActivity.this);
	}

	protected void findViewById() {
	}

	protected void jump() {
		// 启动三秒后进度到登陆界面
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (UserInfoManager.getIsFirstUse(SplashActivity.this)) {
					UserInfoManager.setIsFirstUse(SplashActivity.this, false);
					openActivity(GuideViewActivity.class);
					SplashActivity.this.finish();
				} else {
					openActivity(HomeActivity.class);
					SplashActivity.this.finish();
				}
			}
		}, SPLISH_DISPLAY_LENGTH);

	}

	private void initData() {
		checkUpdateVersion();
	}

	private void checkUpdateVersion() {
		AppLogic.getVersion(SplashActivity.this, mVersionHandler,
				SystemUtils.getVersion(getApplicationContext()));
	}

}
