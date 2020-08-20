package com.example.futureweather

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle


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
        initLifeCycle()
    }

    private fun initLifeCycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity) {}

            override fun onActivityStarted(p0: Activity) {}

            override fun onActivityDestroyed(p0: Activity) {}

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

            override fun onActivityStopped(p0: Activity) {}

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
               MyActivityManager.INSTANT.setCurrentActivity(p0)
            }

            override fun onActivityResumed(p0: Activity) {
                MyActivityManager.INSTANT.setCurrentActivity(p0)
            }

        })
    }
}