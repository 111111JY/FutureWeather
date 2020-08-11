package com.example.futureweather

import android.app.Application
import android.content.Context
import android.content.res.Configuration


/**
 * 获取全局context
 */
class MyApplication : Application() {
    companion object {
        lateinit var context: Context
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