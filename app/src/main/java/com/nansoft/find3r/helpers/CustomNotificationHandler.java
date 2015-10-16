package com.nansoft.find3r.helpers;

import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.activity.MainActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Carlos on 07/10/2015.
 */
public class CustomNotificationHandler extends NotificationsHandler
{
    public static  int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String idNoticia = bundle.getString("id");
        String nhMessage = bundle.getString("message");

        sendNotification(idNoticia,nhMessage);
    }

    private void sendNotification(String idNoticia,String msg) {

        Toast.makeText(ctx,"id " + idNoticia,Toast.LENGTH_SHORT).show();
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);


        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, ComentarioActivity.class).putExtra("idNoticia",idNoticia), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Find3r")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
