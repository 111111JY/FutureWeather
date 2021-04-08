package com.example.futureweather.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.futureweather.R

class NoteNotification(context: Context, placeName: String, rainProbability: Float) {
    private val channelId = "weather_rain_probability_notification"
    private val channelName = "未来2小时天气提醒通知"
    private val notificationId = 1
    private val mContext = context
    private var contentText = when (rainProbability) {
        in 0..10 -> placeName + context.getString(R.string.note_rain_forecast_10)
        in 11..54 -> placeName + context.getString(R.string.note_rain_forecast_35)
        in 55..74 -> placeName + context.getString(R.string.note_rain_forecast_55)
        in 75..100 -> placeName + context.getString(R.string.note_rain_forecast_75)
        else -> ""
    }

    fun init() {
        val manager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(mContext, channelId)
            .setContentTitle(mContext.getString(R.string.app_name))
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    Resources.getSystem(),
                    R.mipmap.ic_launcher_round
                )
            )
            .build()

        manager.notify(notificationId, notification)
    }

}