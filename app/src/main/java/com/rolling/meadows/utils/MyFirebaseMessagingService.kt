package com.rolling.meadows.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rolling.meadows.BuildConfig
import com.rolling.meadows.R
import com.rolling.meadows.cache.CacheConstants
import com.rolling.meadows.cache.Prefs
import com.rolling.meadows.views.view_main.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private fun displayMessage(extras: Bundle) {
        val intent = Intent(Constants.DISPLAY_MESSAGE_ACTION)
        intent.putExtra("detail", extras)
        sendBroadcast(intent)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        try {
            if (intent!!.extras != null) {
                val builder = RemoteMessage.Builder("MyFirebaseMessagingService")
                for (key in intent.extras!!.keySet()) {
                    builder.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(builder.build())

            } else {
                super.handleIntent(intent)
            }
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Prefs.save(CacheConstants.DEVICE_TOKEN, token)
        if (BuildConfig.DEBUG) {
            Log.e("catch_exception", "token :$token");
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        if (BuildConfig.DEBUG) {
            Log.e("catch_exception", "Notification data :$data");
        }


        val bundle = Bundle()
        for ((key, value) in remoteMessage.data) {
            bundle.putString(key, value)
        }

        displayMessage(bundle)

        sendNotification(bundle)

    }


    @SuppressLint("InvalidWakeLockTag")
    private fun sendNotification(bundle: Bundle) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("detail", bundle)
        intent.putExtra("isPush", true)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val message = bundle.getString("description")
        val title = bundle.getString("title")
        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.notification_icon_overlay)
        val vibrate = longArrayOf(100, 100, 100, 100, 100)
        val mPowerManager = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        var wl: PowerManager.WakeLock? = null
        if (mPowerManager != null) {
            wl = mPowerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "tag"
            )
        }
        wl?.acquire(1000)


        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon_overlay)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setVibrate(vibrate)
            .setShowWhen(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSound(defaultSoundUri)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setLargeIcon(largeIcon)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.setShowBadge(true)
            channel.enableVibration(true)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.shouldVibrate()
            channel.setSound(defaultSoundUri, null)

            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(10, notificationBuilder.build())
    }
}