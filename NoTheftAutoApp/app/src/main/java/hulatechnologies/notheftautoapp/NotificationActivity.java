package hulatechnologies.notheftautoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * Created by henni on 15/03/2016.
 */
public class NotificationActivity extends AppCompatActivity {

    private NotificationManager myNotificationManager;


    public void notification(){

        int requestID = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(getApplicationContext(), Status.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[]{1, 1, 1})
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentTitle("OI! YOU CUNT!")
                .setContentText("YER CAR IS BEIN' STOLEN!")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(contentIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
