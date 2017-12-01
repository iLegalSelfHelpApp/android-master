package com.cs401.ilegal.ilegal;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

/**
 * Created by seanyuan on 9/21/17.
 */

public class FirebaseNotifications extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("NOTIFICATION");
        String [] data = (remoteMessage.getNotification().getTitle()).split(">");
        if(data.length > 1){
            String title = data[0];
            int time = Integer.parseInt(data[1]);
            String body = remoteMessage.getNotification().getBody();
            if(time > 0){
                System.out.println("Recurring notification scheduled: " + title + ". Repeats monthly.");
                AlarmReceiver.scheduledtitle = title;
                AlarmReceiver.scheduledbody = body;
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), time, sender);
            }else{
                System.out.println("One time notification: " + title);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.civil)
                                .setContentTitle(title)
                                .setContentText(body);

                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
                NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, mBuilder.build());
            }
        }else{
            System.out.println("Firebase notification: " + remoteMessage.getNotification().getTitle());
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.civil)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody());

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);
            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, mBuilder.build());
        }



    }
}