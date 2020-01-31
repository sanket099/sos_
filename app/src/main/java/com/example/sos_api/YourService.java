package com.example.sos_api;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;



import java.util.TimerTask;

    public class YourService extends Service {
        public int counter=0;

        @Override
        public void onCreate() {
            super.onCreate();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                startMyOwnForeground();
            else
                startForeground(1, new Notification());

        }

        @RequiresApi(Build.VERSION_CODES.O)
        private void startMyOwnForeground()


        {
            NotificationManagerCompat notificationManagerCompat;
            notificationManagerCompat = NotificationManagerCompat.from(YourService.this);
            String NOTIFICATION_CHANNEL_ID = "example.permanence";
            String channelName = "Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Context context = getApplicationContext();
            Intent myIntent = new Intent(context, map.class);
            PendingIntent intent = PendingIntent.getActivity(context,1,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            String lattitude = shared_pref.getInstance(this).get_saved_lattitude_event();
            String longitude = shared_pref.getInstance(this).get_saved_longi_event();
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("App is running in background")
                    .setContentText("http://maps.google.com/maps?saddr="+lattitude+","+longitude)
                    .setPriority(NotificationManager.IMPORTANCE_MIN).setSmallIcon(R.drawable.icon).setContentIntent(intent)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(1, notification);
        }


        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);
            startTimer();
            return START_STICKY;
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            stoptimertask();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }



        private Timer timer;
        private TimerTask timerTask;
        public void startTimer() {
            timer = new Timer();
            timerTask = new TimerTask() {
                public void run() {
                    Log.i("Count", "=========  "+ (counter++));
                }
            };
            timer.schedule(timerTask, 1000, 1000); //
        }

        public void stoptimertask() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }




