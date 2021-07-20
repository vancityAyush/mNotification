package com.ak11.mnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class mNotification extends ContextWrapper {

    private NotificationChannel mNotificationChannel;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "DEFAULT";
    private static final String CHANNEL_NAME = "Default";
    private boolean isCallable;
    Notification.Builder notification;

    public mNotification(Context base) {
        super(base);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationChannel = null;
        isCallable = false;
    }

    private void createNotificationChannel(String channelID, String channelName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationChannel = new NotificationChannel(CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mNotificationChannel);
        }
    }

    // Normal Notification
    private Notification.Builder getNotification (String title, String body){
        Notification.Builder notification = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(mNotificationChannel == null)
                createNotificationChannel(CHANNEL_ID,CHANNEL_NAME);
                 notification = new Notification.Builder(this,CHANNEL_ID)
                    .setColorized(true);

        }else {

            notification = new Notification.Builder(this);
        }

        notification.setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setAutoCancel(true);

        if(isCallable){
            notification.setContentIntent(getAction());
        }

        return notification;

    }


    public void createNotification(int ID,String title, String body){
        if(title.isEmpty() || body.isEmpty())
            Toast.makeText(this,"Fields are empty!",Toast.LENGTH_SHORT).show();
        else{
            notification = getNotification(title,body);
           notificationManager.notify(ID,notification.build());
        }
    }

    public void createNotificationWithBigImage(int ID,String title, String body, Bitmap img){
        if(title.isEmpty() || body.isEmpty())
            Toast.makeText(this,"Fields are empty!",Toast.LENGTH_SHORT).show();
        else {
            notification = getNotification(title, body);
            notification.setStyle(new Notification.BigPictureStyle().bigPicture(img).bigLargeIcon(img)).setLargeIcon(img);
            notificationManager.notify(ID, notification.build());
        }

    }

    public void createNotificationWithSmallImage(int ID,String title, String body, Bitmap img){
        if(title.isEmpty() || body.isEmpty())
            Toast.makeText(this,"Fields are empty!",Toast.LENGTH_SHORT).show();
        else {
            notification = getNotification(title, body);
            notification.setLargeIcon(img);
            notificationManager.notify(ID, notification.build());
        }

    }

    public void openNotificationSettings(){

        Intent openSettings = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        openSettings.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
        openSettings.putExtra(Settings.EXTRA_CHANNEL_ID,CHANNEL_ID);
        startActivity(openSettings);




    }

    public void setCallable(boolean flag){
        isCallable = flag;
    }


    public PendingIntent getAction() {
        Intent intent = new Intent(this, notification_open_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        return  pendingIntent;
    }
}
