package com.xgf.winecome;

import com.xgf.winecome.config.Constants;
import com.xgf.winecome.service.MsgService;
import com.xgf.winecome.utils.image.ImageLoaderConfig;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

public class BaseApplication extends Application {

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);

		// Intent intent = new Intent(getApplicationContext(),
		// MsgService.class);
		// getApplicationContext().startService(intent);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
