package com.example.collegenotify.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.collegenotify.Activity.HomeActivity;
import com.example.collegenotify.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String getBGTitle, getBGBody; // Declared local variables

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String notificationTitle = remoteMessage.getNotification().getTitle();
            String notificationBody = remoteMessage.getNotification().getBody();
            sendNotification(notificationTitle, notificationBody);

        }
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        if (intent.getExtras() != null) {
            String notificationLink = null; // Variable to store link data

            for (String key : intent.getExtras().keySet()) {
                String value = intent.getExtras().getString(key);

                // Handling notification title and body
                if ("gcm.notification.title".equals(key)) {
                    getBGTitle = value;
                } else if ("gcm.notification.body".equals(key)) {
                    getBGBody = value;
                }
                // Handle link from notification payload
                else if ("gcm.notification.link".equals(key) || "link".equals(key)) {
                    notificationLink = value;
                }
            }

            // Store data in SQLite including link
            insertData(getBGTitle, getBGBody, notificationLink);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);


    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android Oreo and above, a notification channel is required.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel Human Readable Title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void insertData(String title, String body,String link) {

        if (title != null && body != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            boolean isInserted = dbHelper.insertNotification(title, body, link);
        }
    }
}
