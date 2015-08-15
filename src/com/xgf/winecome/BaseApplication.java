package com.xgf.winecome;

import com.xgf.winecome.config.Constants;
import com.xgf.winecome.utils.image.ImageLoaderConfig;

import android.app.Application;
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
