package com.remindme.utils;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    final Context context = this;
    private static final String TAG = "FirebaseMessageService";
    Map<String, String> objectData;
    String messageBodyData, messageBodyTitle;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            objectData = remoteMessage.getData();
        }

        if (remoteMessage.getNotification() != null) {
            messageBodyData = "Pick you items!!!";
            messageBodyTitle = "Reminder";
        }

        NotificationHelper helper = new NotificationHelper(this);
        NotificationCompat.Builder builder = helper.getNotification(messageBodyTitle, messageBodyData, objectData.get("type"), objectData.get("id"));
        helper.getManager().notify(new Random().nextInt(), builder.build());
    }

}