package com.bikash.trelloclone.fcm



import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bikash.trelloclone.R
import com.bikash.trelloclone.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG,"From: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG,"Message data payload: ${remoteMessage.from} ")

        }

        remoteMessage.notification?.let {
            Log.d(TAG,"Message Notification body: ${it.body} ")
        }
    }

    @SuppressLint("LongLogTag")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG,"Refreshed Token: ${token} ")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?){

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(messageBody:String ){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        val channelId = this.resources.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(
            this, channelId
        ).setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("title")
            .setContentText("Message")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,"Channel Trello Clone",NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())

    }

    companion object{
        private const val TAG = "MyFirebaseMessagingService"
    }
}