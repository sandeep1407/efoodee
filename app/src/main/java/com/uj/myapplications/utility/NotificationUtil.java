package com.uj.myapplications.utility;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import com.uj.myapplications.R;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Umesh Jangid on 17-Mar-2017.
 */

public class NotificationUtil {
    public static void generateNotification(Context context, JSONObject content, Class classToOpen, String str) {
        Intent intent = null;
        PendingIntent pendingIntent = null;
        String NOTIFICATION_CHANNEL_ID = "";
        switch (str) {
            case "0":
                NOTIFICATION_CHANNEL_ID = "my_channel_id_0";
                intent = new Intent(context, classToOpen);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 25, intent, 0);
                break;
            case "1":
                NOTIFICATION_CHANNEL_ID = "my_channel_id_1";
                intent = new Intent(context, classToOpen);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //  intent.putExtra(Constants.FROM, "Notification");
                // intent.putExtra(Constants.EVENT_ID, content.optString("EventId"));
                pendingIntent = PendingIntent.getActivity(context, 25, intent, 0);
                break;
            default:
                intent = new Intent(context, classToOpen);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 25, intent, 0);
                break;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Jairangam", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription(context.getString(R.string.app_name));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_myma_logo_with_tagline)
                .setTicker(context.getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                // .setPriority(Notification.PRIORITY_MAX)
                // .setContentTitle(content.optString(Constants.PUSH_TITLE, context.getString(R.string.app_name)))
                .setContentTitle(content.optString(context.getString(R.string.app_name)))
                .setContentText("Dummy")
                .setContentInfo("Info");
        int id = (int) (new Date().getTime() / 1000);//Said by: LAL sir
        notificationManager.notify(id, notificationBuilder.build());
    }

    public static void clearNotifications(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    public static void askForInput(Context context, String title, String message, String positiveButtonText, String negativeButtonText, boolean showOnlyOneButton, final AlertDialogCallback<String> callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setMessage(message);
        alert.setPositiveButton(positiveButtonText != null ? positiveButtonText : "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.alertDialogCallback("1");
            }
        });
        if (!showOnlyOneButton) {
            alert.setNegativeButton(negativeButtonText != null ? negativeButtonText : "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    callback.alertDialogCallback("0");
                }
            });
        }
        alert.setCancelable(false);
        alert.show();
    }
}
