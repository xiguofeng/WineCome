package com.xgf.winecome.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GuardService extends Service {

	public final static String TAG = "com.xgf.winecome.service.GuardService";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");

		thread.start();
		return START_REDELIVER_INTENT;
	}

	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					Log.e(TAG,
							"GuardService Run: " + System.currentTimeMillis());
					boolean b = isServiceWorked(GuardService.this,
							"com.xgf.wineserver.service.MsgService");
					if (!b) {
						Intent service = new Intent(GuardService.this,
								MsgService.class);
						startService(service);
						Log.e(TAG, "Start MsgService");
					}
				}
			};
			timer.schedule(task, 0, 1000);
		}
	});

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public boolean isServiceWorked(Context context, String serviceName) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(Integer.MAX_VALUE);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

}
