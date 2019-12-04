package com.example.carsharing;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.app.NotificationCompat;

import static android.content.ContentValues.TAG;

public class Notification extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFireBaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        if(remoteMessage.getData().size() > 0)
        {
            try{
                JSONObject data = new JSONObject(remoteMessage.getData());
                String jsonMessage  = data.getString("extra_information");
            } catch(JSONException e){
                e.printStackTrace();;
            }
        }
        if(remoteMessage.getNotification() != null){

            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();
            sendNotification(title, message,click_action);


        }

    }
    private void sendNotification(String title, String messageBody, String click_action) {
        Intent intent;

        intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        Uri nottificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Context context = this;
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O) {
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(context, FirebaseMessagingService.class.getSimpleName())//NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.auto)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                    .setAutoCancel(true)
                    .setSound(nottificationSound)
                    .setChannelId("test")
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            notificationManager.notify(0, notifiBuilder.build());
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default");
        }else
        {

            NotificationUtils mNotificationUtils = new NotificationUtils(this);
            android.app.Notification.Builder nb = mNotificationUtils.
                    getAndroidChannelNotification( title,messageBody,pendingIntent);
            mNotificationUtils.getManager().notify(101, nb.build());

        }

    }

}
