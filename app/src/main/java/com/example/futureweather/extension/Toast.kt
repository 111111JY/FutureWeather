package com.example.futureweather.extension

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.futureweather.FutureWeatherApplication

//确保在主线程中更新UI
private val mainHandler = Handler(Looper.getMainLooper())

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    mainHandler.post { Toast.makeText(FutureWeatherApplication.context, this, duration).show() }
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    mainHandler.post { Toast.makeText(FutureWeatherApplication.context, this, duration).show() }
}