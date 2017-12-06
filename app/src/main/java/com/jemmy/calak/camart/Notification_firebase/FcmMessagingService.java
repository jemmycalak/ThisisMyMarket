package com.example.jemmycalak.thisismymarket.Notification_firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.jemmycalak.thisismymarket.MainActivity;
import com.example.jemmycalak.thisismymarket.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService extends FirebaseMessagingService {
    //class ini untuk otomatis get Notification yang di kirim firebase


    private String title, msg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        //for vibrate
        long[] pattern = {0, 300, 0};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(msg);
        //set Icon
        builder.setSmallIcon(R.drawable.camart);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setSound(defaultSoundUri);
        builder.setVibrate(pattern);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}