package com.remindme.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.remindme.R;
import com.remindme.views.activities.MainActivity;
import com.remindme.views.activities.RemindersListActivity;

import java.util.Calendar;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "com.remindme.CHANNEL_ID";
    private static final String CHANNEL_NAME = "Remind ME Notification";
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build());
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            getManager().createNotificationChannel(notificationChannel);
        }
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getNotification(String title, String body, String type, String id) {

        Intent intent = new Intent(this, RemindersListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantExtras.ARG_PUSH_TYPE, type);
        intent.putExtra(ConstantExtras.ARG_PUSH_ID, id);

        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) Calendar.getInstance().getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);*/
        // Create an Intent for the activity you want to start
        Intent pendingIntent = new Intent(this, RemindersListActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(pendingIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentText("Pick you items!!!")
                    .setContentTitle("Reminder")
                    .setContentIntent(resultPendingIntent)
                    .setColor(ActivityCompat.getColor(this, R.color.notification))
                    .setSmallIcon(R.drawable.logo)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
        } else {
            return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentText("Pick you items!!!")
                    .setContentTitle("Reminder")
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setColor(ActivityCompat.getColor(this, R.color.notification))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
        }
    }
}

/*{
        "to": "eOwAIVkYli8:APA91bG_2p8xjJRI5ccltNrtQtM0fRCtCqDlaUaaccxEll1-WzD-9K86xahKzLWPelmqKfnuURmZDWgsIre8S9dOfycrr5jfTX8DomIhgVXxt1vwLNJiK49NPXQMpv24V6oZsWegCjoLJpFjgpfLic9WgG8vDLg-Kw",

        "notification": {
        "title": "HOTEL TRANSLYVANIA 3: SUMMER VACATION",
        "click_action": "push_launch_activity",
        "body": "Advance bookings are now open. Get your tickets now!",
        "icon":"ic_notification",
        "sound":"default"
        },

        "data": {
        "type": "EVENT",
        "id": "157"
        }

        }*/
