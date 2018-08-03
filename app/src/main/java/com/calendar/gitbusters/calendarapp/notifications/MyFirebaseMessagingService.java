package com.calendar.gitbusters.calendarapp.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.calendar.gitbusters.calendarapp.MainActivity;
import com.calendar.gitbusters.calendarapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * This class is mostly made up of material provided in the link below
 * https://www.mobilhanem.com/android-firebase-cloud-messaging-ile-push-notification-gondermek/
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService  {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /* If the message is a notification */
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Title: "
                    + remoteMessage.getNotification().getTitle() +
                    " " + "Notification Body: " +
                    remoteMessage.getNotification().getBody() );

            sendNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }

    }

    private void sendNotification(String messageTitle,String messageBody) {

        /* Build a new notification intent */
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_upcoming_apt)   // Our beautiful icon
                .setContentTitle(messageTitle)              // Notification title
                .setContentText(messageBody)                // Notification message body
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND|
                        Notification.DEFAULT_LIGHTS|
                        Notification.DEFAULT_VIBRATE)       // Set default alert settings
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}