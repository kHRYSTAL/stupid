package me.khrystal.tools.kaudioplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/12
 * update time:
 * email: 723526676@qq.com
 */
class KNotificationPlayerService implements KPlayerView.KPlayerViewServiceListener {

    static final String NEXT = "NEXT";
    static final String PREVIOUS = "PREVIOUS";
    static final String PAUSE = "PAUSE";
    static final String PLAY = "PLAY";
    static final String ACTION = "ACTION";
    static final String PLAYLIST = "PLAYLIST";
    static final String CURRENT_AUDIO = "CURRENT_AUDIO";

    private static final int NOTIFICATION_ID = 100;
    private static final int NEXT_ID = 0;
    private static final int PREVIOUS_ID = 1;
    private static final int PLAY_ID = 2;
    private static final int PAUSE_ID = 3;

    private NotificationManager notificationManager;
    private Context context;
    private String title;
    private String time = "00:00";
    private int iconResource;
    private Notification notification;
    private NotificationCompat.Builder notificationCompat;

    public KNotificationPlayerService(Context context) {
        this.context = context;
    }

    public void createNotificationPlayer(String title, int iconResource) {
        this.title = title;
        this.iconResource = iconResource;
        Intent openUI = new Intent(context, context.getClass());
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        KAudioPlayer.getInstance().registerNotificationListener(this);

        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification = new Notification.Builder(context)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setSmallIcon(iconResource)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconResource))
                    .setContent(createNotificationPlayerView())
                    .setContentIntent(PendingIntent.getActivity(context, NOTIFICATION_ID, openUI, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setCategory(Notification.CATEGORY_SOCIAL)
                    .build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            notificationCompat = new NotificationCompat.Builder(context)
                    // TODO: 19/4/12 set to API below Build.VERSION.SDK_INT
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setSmallIcon(iconResource)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconResource))
                    .setContent(createNotificationPlayerView())
                    .setContentIntent(PendingIntent.getActivity(context, NOTIFICATION_ID, openUI, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setCategory(Notification.CATEGORY_SOCIAL);
            notificationManager.notify(NOTIFICATION_ID, notificationCompat.build());
        }
    }

    public void updateNotification() {
        createNotificationPlayer(title, iconResource);
    }

    private RemoteViews createNotificationPlayerView() {
        RemoteViews remoteView;
        if (KAudioPlayer.getInstance().isPaused()) {
            remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_notification_play);
            remoteView.setOnClickPendingIntent(R.id.btnPlayNotification, buildPendingIntent(PLAY, PLAY_ID));
        } else {
            remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_notification_pause);
            remoteView.setOnClickPendingIntent(R.id.btnPauseNotification, buildPendingIntent(PAUSE, PAUSE_ID));
        }
        remoteView.setTextViewText(R.id.tvAudioTitleNotification, title);
        remoteView.setTextViewText(R.id.tvTotalDurationNotification, time);
        remoteView.setImageViewResource(R.id.ivIconNotification, iconResource);
        remoteView.setOnClickPendingIntent(R.id.btnNextNotification, buildPendingIntent(NEXT, NEXT_ID));
        remoteView.setOnClickPendingIntent(R.id.btnPrevNotification, buildPendingIntent(PREVIOUS, PREVIOUS_ID));

        return remoteView;
    }

    private PendingIntent buildPendingIntent(String action, int id) {
        Intent playIntent = new Intent(context.getApplicationContext(), KPlayerNotificationReceiver.class);
        playIntent.putExtra(ACTION, action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), id, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onPreparedAudio(String audioName, int duration) {

    }

    @Override
    public void onCompletedAudio() {

    }

    @Override
    public void onPaused() {
        createNotificationPlayer(title, iconResource);
    }

    @Override
    public void onContinueAudio() {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onTimeChanged(long currentTime) {
        long aux = currentTime / 1000;
        int minutes = (int) (aux / 60);
        int seconds = (int) (aux % 60);
        final String sMinutes = minutes < 10 ? "0" + minutes : minutes + "";
        final String sSeconds = seconds < 10 ? "0" + seconds : seconds + "";
        this.time = sMinutes + ":" + sSeconds;

        createNotificationPlayer(title, iconResource);
    }

    @Override
    public void updateTitle(String title) {
        this.title = title;
        createNotificationPlayer(title, iconResource);
    }

    public void destroyNotificationIfExists() {
        if (notificationManager != null) {
            try {
                notificationManager.cancel(NOTIFICATION_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
