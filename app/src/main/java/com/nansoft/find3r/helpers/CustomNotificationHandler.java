package com.nansoft.find3r.helpers;

import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.activity.MainActivity;
import com.nansoft.find3r.activity.NotificationsActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Carlos on 07/10/2015.
 */
public class CustomNotificationHandler extends NotificationsHandler
{
    public static  int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    ArrayList <String> collectionNotifications = new ArrayList<String>();

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String idNoticia = bundle.getString("id");
        String nhMessage = bundle.getString("message");
        collectionNotifications.add(nhMessage);

        sendNotification(idNoticia, nhMessage);
    }

    private void sendNotification(String idNoticia,String msg) {


        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);


        PendingIntent contentIntent;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher);

        NotificationCompat.InboxStyle inBoxStyle =
                new NotificationCompat.InboxStyle();

        int tamanio = collectionNotifications.size();

        String mensajeAux = tamanio + " interacción";

        if (tamanio > 1)
        {
            mensajeAux = tamanio + " interacciones";
            contentIntent = PendingIntent.getActivity(ctx, 0,
                    new Intent(ctx, NotificationsActivity.class).putExtra("idNoticia", idNoticia), 0);

        }
        else
        {
            contentIntent = PendingIntent.getActivity(ctx, 0,
                    new Intent(ctx, ComentarioActivity.class).putExtra("idNoticia", idNoticia), 0);
        }
        mBuilder.setContentIntent(contentIntent);

        inBoxStyle.setBigContentTitle(mensajeAux);

        for (int i=0; i < tamanio ; i++) {

            inBoxStyle.addLine(collectionNotifications.get(i));
        }
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inBoxStyle);




        mBuilder.setAutoCancel(true);

        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

       // mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }



}
