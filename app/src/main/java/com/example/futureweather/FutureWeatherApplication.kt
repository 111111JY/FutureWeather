package com.example.futureweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration


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

    /**
     * 判断当前系统是否为黑夜模式
     */
    fun isDarkTheme(): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}