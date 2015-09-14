package com.xgf.winecome.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.NotifyMsg;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.network.logic.MsgLogic;
import com.xgf.winecome.utils.UserInfoManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

public class MsgService extends Service {

	public final static String TAG = "com.xgf.winecome.service.MsgService";

	private static final int NOTIFY_UPDATE = 1;

	public static final String NOTIFY_DATA_UPDATE = "action.notify.data.update";

	public static final String NOTIFY_SETDESK_SUC = "action.notify.data.setdesk.suc";

	public static final String NOTIFY_RANK_SORT = "action.notify.data.rank.sort";

	private Context mContext;

	private NotificationManager mNotificationManager;

	private NotificationCompat.Builder mBuilder;

	private static boolean sIsClose = false;

	public int count = 0;

	private static HashMap<String, Object> sOrderMsgMap = new HashMap<String, Object>();

	public static ArrayList<Order> sOrderList = new ArrayList<Order>();

	public ArrayList<NotifyMsg> mNotifyMsgList = new ArrayList<NotifyMsg>();

	private Handler mMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NOTIFY_UPDATE: {
				if (!sIsClose) {
					if (!TextUtils.isEmpty(UserInfoManager.getPhone(mContext))) {
						sendHeartbeatPackage();
					}
					mMsgHandler.sendEmptyMessageDelayed(NOTIFY_UPDATE, 30000);
				}
				break;
			}
			default:
				break;
			}
		};

	};

	private Handler mHeartBeatHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case MsgLogic.MSG_GET_SUC: {
				if (null != msg.obj) {
					ArrayList<NotifyMsg> notifyMsgList = new ArrayList<NotifyMsg>();
					notifyMsgList.addAll((Collection<? extends NotifyMsg>) msg.obj);
					for (NotifyMsg notifyMsg : notifyMsgList) {
						showIntentActivityNotify(notifyMsg);
					}

				}
				break;
			}
			case MsgLogic.MSG_GET_FAIL: {

				break;
			}
			case MsgLogic.MSG_GET_EXCEPTION: {
				break;
			}
			case MsgLogic.NET_ERROR: {
				break;
			}
			default:
				break;
			}

		};

	};

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		initNotify();
		String version = getVersion();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");

		// monitorThread.start();
		//mMsgHandler.sendEmptyMessage(NOTIFY_UPDATE);
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent sevice = new Intent(this, MsgService.class);
		this.startService(sevice);
	}

	private void sendHeartbeatPackage() {
		MsgLogic.getPushMsg(mContext, mHeartBeatHandler, UserInfoManager.getPhone(mContext));
	}

	/** 初始化通知栏 */
	private void initNotify() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);
	}

	/** 显示通知栏点击跳转到指定Activity */
	private void showIntentActivityNotify(NotifyMsg notifyMsg) {
		// Notification.FLAG_ONGOING_EVENT --设置常驻
		// Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// //在通知栏上点击此通知后自动清除此通知
		mBuilder.setAutoCancel(true)
				// 点击后让通知将消失
				.setContentTitle(notifyMsg.getMsgTime()).setContentText("消息内容：" + notifyMsg.getContent())
				.setTicker("酒来了").setSmallIcon(R.drawable.logo_app).setDefaults(Notification.DEFAULT_VIBRATE);
		// 点击的意图ACTION是跳转到Intent
		Intent resultIntent = new Intent();
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);
		//Log.e("xxx_mNotificationManager", "mNotificationManager");
		mNotificationManager.notify(100, mBuilder.build());
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	private String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			return null;
		}
	}

	Thread monitorThread = new Thread(new Runnable() {

		@Override
		public void run() {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					Log.e(TAG, "MsgService Run: " + System.currentTimeMillis());
					boolean b = isServiceWorked(MsgService.this, "com.xgf.wineserver.service.GuardService");
					if (!b) {
						Intent service = new Intent(MsgService.this, GuardService.class);
						startService(service);
						Log.e(TAG, "Start GuardService");
					}
				}
			};
			timer.schedule(task, 0, 1000);
		}
	});

	public boolean isServiceWorked(Context context, String serviceName) {
		ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(Integer.MAX_VALUE);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

}
