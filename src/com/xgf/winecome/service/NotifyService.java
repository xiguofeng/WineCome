
package com.xgf.winecome.service;

import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.NotifyInfo;
import com.xgf.winecome.notify.Notify;
import com.xgf.winecome.notify.NotifyFactory;
import com.xgf.winecome.notify.NotifyFactory.NotifyType;
import com.xgf.winecome.notify.notification.ManagerNotification;

public class NotifyService extends Service {

    private static final int NOTIFY_UPDATE = 1;

    public static final String NOTIFY_DATA_UPDATE = "action.notify.data.update";

    public static final String NOTIFY_SETDESK_SUC = "action.notify.data.setdesk.suc";

    public static final String NOTIFY_RANK_SORT = "action.notify.data.rank.sort";

    private NotificationManager mNotificationManager;

    private ManagerNotification mManagerNotification;

    private NotifyNotification mNotifier;

    UpdateThread mUpdateThread;

    public static HashMap<Integer, NotifyInfo> sNotifyHistory = new HashMap<Integer, NotifyInfo>();

    HashMap<Integer, Notification> mNotifications = new HashMap<Integer, Notification>();

    private static int sNotifyId = 0;

    private static final int NOTIFY_GET_FAIL = 0;

    private static final int NOTIFY_GET_SUC = NOTIFY_GET_FAIL + 1;

    private static final int NOTIFY_GET_EXCEPTION = NOTIFY_GET_SUC + 1;

    private IBinder binder = new NotifyService.LocalBinder();

    private String path;

    private static boolean sIsClose = false;

    public int count = 0;

    private Handler mMsgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOTIFY_UPDATE: {
                    if (!sIsClose) {
                        sendHeartbeatPackage();
                        mMsgHandler.sendEmptyMessageDelayed(NOTIFY_UPDATE, 5000);
                    }
                    break;
                }
                default:
                    break;
            }
        };

    };

    private Handler mHeartHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HeartbeatLogic.GET_HEART_SUCCESS: {
                    if (null != msg.obj) {
                        sendMsg((HashMap<String, Object>) msg.obj);
                    }
                    break;
                }
                case HeartbeatLogic.GET_HEART_FAIL: {
                    Toast.makeText(NotifyService.this, getString(R.string.remote_get_fail),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case HeartbeatLogic.GET_HEART_EXCEPTION: {
                    Toast.makeText(NotifyService.this, getString(R.string.json_exception),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case HeartbeatLogic.NET_ERROR: {
                    Toast.makeText(NotifyService.this, getString(R.string.net_err),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };

    };

    // 定义内容类继承Binder
    public class LocalBinder extends Binder {
        // 返回本地服务
        public NotifyService getService() {
            return NotifyService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {

        boolean b = super.onUnbind(intent);
        return b;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sIsClose = true;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // initNotification();
        // mNotifier = new NotifyNotification(NotifyService.this,
        // mManagerNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // path = intent.getStringExtra("url");
        mMsgHandler.sendEmptyMessage(NOTIFY_UPDATE);
        sIsClose = false;
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendHeartbeatPackage() {
        HeartbeatLogic.sendHeartbeatPackage(NotifyService.this, mHeartHandler);
    }

    private void sendMsg(HashMap<String, Object> info) {

        String typeString = (String) info.get(MsgResult.RESULT_TYPE_DATA_TAG);
        String dateString = (String) info.get(MsgResult.RESULT_DATE_DATA_TAG);
        Log.e("typeString", "" + typeString);
        if (!TextUtils.isEmpty(typeString)) {
            String[] strings = typeString.substring(0, typeString.length()).split(",");
            for (int i = 0; i < strings.length; i++) {
                switch (Integer.valueOf(strings[i])) {
                    case 0: {

                        // TODO
                        break;
                    }
                    case 1: {
                        Notify queneNotify = NotifyFactory.create(NotifyType.Quene);
                        queneNotify.sendMsg(NotifyService.this,
                                info.get(MsgResult.RESULT_QUEUE_DATA_TAG));

                        Rank rank = (Rank) info.get(MsgResult.RESULT_QUEUE_DATA_TAG);
                        NotifyInfo notifyInfo = new NotifyInfo();
                        notifyInfo.setSort(rank.getSort().toString());
                        notifyInfo.setNotifyType(NotifyType.Quene);
                        notifyInfo.setNum(rank.getNum());
                        notifyInfo.setDate(dateString);
                        notifyInfo.setTitle(getResources().getString(R.string.notify_queue));
                        notifyInfo.setContent(rank.getSort().toString() + getResources().getString(
                                R.string.notify_queue_msg_behind));
                        showNotification(notifyInfo);
                        break;
                    }
                    case 2: {
                        Notify deskNotify = NotifyFactory.create(NotifyType.Desk);
                        deskNotify.sendMsg(NotifyService.this,
                                info.get(MsgResult.RESULT_DESK_DATA_TAG));
                        ProcessManager.addProcessStep(ProcessManager.FIFTHSTEP_CANSETDESK);

                        Desk desk = (Desk) info.get(MsgResult.RESULT_DESK_DATA_TAG);
                        NotifyInfo notifyInfo = new NotifyInfo();
                        notifyInfo.setDeskNo(desk.getSno());
                        notifyInfo.setNotifyType(NotifyType.Desk);
                        notifyInfo.setDeskForNum(desk.getNum());
                        notifyInfo.setDate(dateString);
                        notifyInfo.setTitle(getResources().getString(R.string.notify_queue));
                        notifyInfo.setContent(desk.getSno() + getResources().getString(
                                R.string.notify_queue_desk_msg_behind));
                        showNotification(notifyInfo);

                        break;
                    }
                    case 3: {
                        Notify addFoodNotify = NotifyFactory.create(NotifyType.AddFood);

                        String addFoodInfo = (String) info.get(MsgResult.RESULT_ISADDFOOD_DATA_TAG);
                        if (!TextUtils.isEmpty(addFoodInfo) && addFoodInfo.contains(",")) {
                            String[] infos = addFoodInfo.substring(0, addFoodInfo.length())
                                    .split(",");
                            for (String s : infos) {
                                if (!"".equals(s)) {
                                    addFoodNotify.sendMsg(NotifyService.this,
                                            s);

                                    NotifyInfo notifyInfo = new NotifyInfo();
                                    notifyInfo.setDate(dateString);
                                    notifyInfo.setNotifyType(NotifyType.AddFood);
                                    notifyInfo.setTitle(getResources().getString(
                                            R.string.notify_add_food));
                                    notifyInfo.setContent(s);
                                    showNotification(notifyInfo);
                                }
                            }
                        } else if (!TextUtils.isEmpty(addFoodInfo) && !addFoodInfo.contains(",")) {
                            addFoodNotify.sendMsg(NotifyService.this,
                                    addFoodInfo);

                            NotifyInfo notifyInfo = new NotifyInfo();
                            notifyInfo.setDate(dateString);
                            notifyInfo.setNotifyType(NotifyType.AddFood);
                            notifyInfo.setTitle(getResources().getString(
                                    R.string.notify_add_food));
                            notifyInfo.setContent(addFoodInfo);
                            showNotification(notifyInfo);
                        }

                        break;
                    }
                    case 4: {
                        Notify commentNotify = NotifyFactory.create(NotifyType.Comment);
                        commentNotify.sendMsg(NotifyService.this,
                                info.get(MsgResult.RESULT_ISCOMMENT_DATA_TAG));

                        NotifyInfo notifyInfo = new NotifyInfo();
                        notifyInfo.setTitle(getResources().getString(R.string.notify_comment));
                        notifyInfo
                                .setContent(getResources().getString(R.string.notify_can_comment));
                        notifyInfo.setDate(dateString);
                        notifyInfo.setNotifyType(NotifyType.Comment);
                        showNotification(notifyInfo);
                        ProcessManager.addProcessStep(ProcessManager.SEVENTHSTEP_CANCOMMENT);
                        break;
                    }
                    default:
                        break;
                }
            }
        }

    }

    private void showNotification(NotifyInfo notifyInfo) {

        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;// 表示正处于活动中
        notification.defaults |= Notification.DEFAULT_SOUND;

        Intent intent = new Intent(this, NotifyListActivity.class);
        // 这里要加入此Flags，作用：当你通过点击通知栏来唤起Activity时，对应的Activity启动模式要为android:launchMode="singleTop"
        // 于此Flag一起使用，可以保证你要启动的Activity不会重新启动，在一个堆栈只有一个对应的实例对象
        // 使用这个标志，如果正在启动的Activity的Task已经在运行的话，那么，新的Activity将不会启动；代替的，当前Task会简单的移入前台。
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, sNotifyId + 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
        contentView.setImageViewResource(R.id.nf_icon, R.drawable.ic_launcher);
        notification.contentView = contentView;

        long time = SystemClock.elapsedRealtime();

        // notification.contentView.setTextViewText(R.id.nf_content,
        // notifyInfo.getTitle());
        notification.contentView.setTextViewText(R.id.nf_content, notifyInfo.getTitle() + ":"
                + notifyInfo.getContent());

        // 使用服务来启动通知栏，这样的好处时，
        // 1.当应用程序在后台运行时，startForeground可以使本应用优先级提高，不至于被系统杀掉
        // 2.当应用被异常挂掉时，可以保证通知栏对应的图标被系统移除
        startForeground(sNotifyId + 1, notification);

        sNotifyHistory.put(sNotifyId, notifyInfo);
        mNotifications.put(sNotifyId, notification);
        sNotifyId++;

    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(
                Context.NOTIFICATION_SERVICE);
        mManagerNotification = new ManagerNotification(mNotificationManager);
    }

    private void update() {
        synchronized (this) {
            if (mUpdateThread == null) {
                mUpdateThread = new UpdateThread();
                mUpdateThread.start();
            }
        }
    }

    private class UpdateThread extends Thread {
        public UpdateThread() {
            super("Download Service");
        }

        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            for (;;) {
                synchronized (NotifyService.this) {
                    if (mUpdateThread != this) {
                        throw new IllegalStateException("multiple UpdateThreads in DownloadService");
                    }
                }
                // mNotifier.updateNotification(notifyNow);
            }
        }
    }

}
