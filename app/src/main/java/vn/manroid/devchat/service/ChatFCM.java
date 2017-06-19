package vn.manroid.devchat.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import vn.manroid.devchat.R;
import vn.manroid.devchat.activity.SplashActivity;

/**
 * Created by manro on 17/06/2017.
 */

public class ChatFCM extends FirebaseMessagingService {
    String TAG = "MyFirebase";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage.getNotification() != null){
                Log.e(TAG,remoteMessage.getNotification().getBody());
                // Log.e(TAG,remoteMessage.getNotification().getTitle());
                sendMessage(remoteMessage.getNotification().getBody());


            }

            if (remoteMessage.getData().size() > 0){
                Log.e(TAG,remoteMessage.getData() + "");
                Log.e(TAG,remoteMessage.getData().get("k1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String messageBody){
        try {
            Intent intent = new Intent(this,SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("FCM Manager")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
