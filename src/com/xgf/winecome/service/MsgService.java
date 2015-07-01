package com.xgf.winecome.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MsgService extends Service {

	public final static String TAG = "com.xgf.winecome.service.MsgService";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");

		thread.start();
		return START_STICKY;
	}

	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					Log.e(TAG, "MsgService Run: " + System.currentTimeMillis());
					boolean b = false;

					// MainActivity.isServiceWorked(MsgService.this,
					// "com.example.servicedemo.ServiceTwo");
					if (!b) {
						Intent service = new Intent(MsgService.this,
								GuardService.class);
						startService(service);
						Log.e(TAG, "Start GuardService");
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
