package com.example.carsharing.Other;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.carsharing.Activity.Login;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.R;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import androidx.core.app.NotificationCompat;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFireBaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                String jsonMessage  = data.getString("title");
                //sendNotification(data.getString("title"), data.getString("body"),data.getString("click_action"));
            }catch (Exception e){
                Logs_DataHandler log = new Logs_DataHandler(this);
                log.inputLog( "FirebaseMessagingService.class 001: "+e.toString());
                log.close();
            }
        }

        if(remoteMessage.getNotification() != null){
                try {
                    String title = remoteMessage.getNotification().getTitle();

                    String message = remoteMessage.getNotification().getBody();
                    //String Langi = remoteMessage.getNotification().;
                    //String Longi;
                    String click_action = remoteMessage.getNotification().getClickAction();
                    sendNotification(title, message, click_action);
                }catch(Exception e){
                    Logs_DataHandler log = new Logs_DataHandler(this);
                    log.inputLog( "FirebaseMessagingService.class 002: "+e.toString());
                    log.close();
                }
            }

        }



    private void sendNotification(String title, String messageBody, String click_action) {


          Intent intent;
            intent = new Intent(this, Login.class);
        intent.putExtra("title",title);
        intent.putExtra("body",messageBody);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        Uri nottificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Context context = this;
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O) {
            try {
                NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(context, FirebaseMessagingService.class.getSimpleName())//NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.auto)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setAutoCancel(true)
                        .setSound(nottificationSound)
                        .setVibrate(new long[]{1000, 100, 1000, 100, 1000})
                        .setChannelId("test")
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_LOW;
                notificationManager.notify(0, notifiBuilder.build());
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(FirebaseMessagingService.this, "default");
            }
            catch (Exception e){
                Logs_DataHandler log = new Logs_DataHandler(this);
                log.inputLog( "FirebaseMessagingService.class 003: "+e.toString());
                log.close();
            }
        }else
        {
            try {
                NotificationUtils mNotificationUtils = new NotificationUtils(this);
                Notification.Builder nb = mNotificationUtils.
                        getAndroidChannelNotification(title, messageBody, pendingIntent);
                mNotificationUtils.getManager().notify(101, nb.build());
            }catch(Exception e){
                Logs_DataHandler log = new Logs_DataHandler(this);
                log.inputLog( "FirebaseMessagingService.class 004: "+e.toString());
                log.close();
            }
        }}
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private  void  sendRegistrationToServer(String token){

    }
}
