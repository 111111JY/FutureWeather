package com.example.futureweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import android.view.Window


/**
 * 获取全局context
 */
class FutureWeatherApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //彩云天气API的 TOKEN
        const val TOKEN = "TH9Qw0cSKnY98jQG"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}