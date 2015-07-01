package com.xgf.winecome.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
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
					Log.e(TAG, "GuardService Run: " + System.currentTimeMillis());
					boolean b = false;
					// MainActivity.isServiceWorked(GuardService.this,
					// "com.example.servicedemo.ServiceOne");
					if (!b) {
						Intent service = new Intent(GuardService.this,
								MsgService.class);
						startService(service);
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

}
