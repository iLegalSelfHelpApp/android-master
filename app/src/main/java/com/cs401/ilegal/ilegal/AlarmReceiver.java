package com.cs401.ilegal.ilegal;

/**
 * Created by seanyuan on 9/28/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    static String scheduledtitle;
    static String scheduledbody;
   @Override
   public void onReceive(Context context, Intent intent) {
       if(scheduledtitle != null){
           System.out.println("I'm running");
           NotificationCompat.Builder mBuilder =
                   new NotificationCompat.Builder(context)
                           .setSmallIcon(R.drawable.civil)
                           .setContentTitle(scheduledtitle)
                           .setContentText(scheduledbody);

           Intent notificationIntent = new Intent(context, MainActivity.class);
           PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                   PendingIntent.FLAG_UPDATE_CURRENT);
           mBuilder.setContentIntent(contentIntent);

           // Add as notification
           NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           manager.notify(0, mBuilder.build());
       }
   }
}