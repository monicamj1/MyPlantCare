package com.example.monicamj1.myplantcare;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class Notifications extends Application {

    public static final String CHANNEL_ID = "1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationsChannel();

    }

    //TODO: poner todo el tema de gestión de notificaciones aqui con la consulta a la db y el calculo de los dias restantes. Copiarlo de MyGarden

    private void createNotificationsChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel_1", importance);
            channel.setDescription("This is channel 1");
            channel.setLockscreenVisibility( Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
