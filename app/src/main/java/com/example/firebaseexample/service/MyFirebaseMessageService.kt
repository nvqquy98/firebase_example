package com.example.firebaseexample.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.firebaseexample.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import androidx.core.content.ContextCompat
import com.example.firebaseexample.R
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.firebaseexample.model.MessageData
import com.google.gson.Gson

class MyFirebaseMessageService : FirebaseMessagingService() {
    private val gson = Gson()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = gson.fromJson(gson.toJson(remoteMessage.data), MessageData::class.java)
        sendNotification(data)
    }

    private fun sendNotification(messageData: MessageData) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        val bundle = Bundle().apply {
            putParcelable(FCM_PARAM, messageData)
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtras(bundle)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder =
            NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle(messageData.title)
                .setContentText(messageData.description)
                .setGroup("com.android.example.WORK_EMAIL")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_notifications)
                .setStyle(
                    NotificationCompat.InboxStyle().addLine("aaa")
                        .addLine("aaa").addLine("aaa")
                )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.notification_channel_id),
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
                setShowBadge(true)
                canShowBadge()
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            }
            notificationManager?.createNotificationChannel(channel)
        }
        Glide.with(applicationContext).asBitmap().load(messageData.imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    notificationBuilder.setStyle(
                        NotificationCompat.BigPictureStyle().bigPicture(resource)
                            .setSummaryText("Hello")
                    )
                    notificationManager?.notify(numMessages, notificationBuilder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
    }

    companion object {
        const val FCM_PARAM = "picture"
        private const val CHANNEL_NAME = "FCM"
        private const val CHANNEL_DESC = "Firebase Cloud Messaging"
        private var numMessages = 0
    }
}
