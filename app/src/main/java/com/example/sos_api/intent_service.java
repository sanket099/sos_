/*
package com.example.sos_api;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class intent_service extends IntentService {

    //Context context = getApplicationContext();
    */
/**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * //@param name Used to name the worker thread, important only for debugging.
     *//*

    public intent_service() {
        super("name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent1) {

        NotificationManagerCompat notificationManagerCompat;
        notificationManagerCompat = NotificationManagerCompat.from(intent_service.this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "channel1","channel1", NotificationManager.IMPORTANCE_DEFAULT
            );
            String loc = "hello";//"http://maps.google.com/maps?saddr="+lattitude+","+longitude;
            //channel1.setDescription("http://maps.google.com/maps?saddr="+lattitude+","+longitude);
            channel1.setDescription("http://maps.google.com/maps?saddr=");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
        //String loc = "http://maps.google.com/maps?saddr="+lattitude+","+longitude;
        String loc = "hello";
        Context context = getApplicationContext();
        intent1 = new Intent(context, map.class);
        PendingIntent intent = PendingIntent.getActivity(context,1,intent1,PendingIntent.FLAG_UPDATE_CURRENT);




        Notification notification = new NotificationCompat.Builder(intent_service.this,"channel1").setContentTitle("help").setContentText(loc)
                .setSmallIcon(R.drawable.icon)
                .setPriority(4).setCategory(NotificationCompat.CATEGORY_MESSAGE).setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                .setContentIntent(intent)
                .build();

        notificationManagerCompat.notify(1,notification);


    }
}
*/
