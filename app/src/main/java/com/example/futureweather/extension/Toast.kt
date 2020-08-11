package com.example.futureweather.extension

import android.widget.Toast
import com.example.futureweather.FutureWeatherApplication

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(FutureWeatherApplication.context, this, duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(FutureWeatherApplication.context, this, duration).show()
}