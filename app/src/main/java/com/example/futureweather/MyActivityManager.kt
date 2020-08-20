package com.example.futureweather

import android.app.Activity
import java.lang.ref.WeakReference

class MyActivityManager {
    companion object {
        val INSTANT = MyActivityManager()
    }

    private lateinit var sCurrentActivityWeakRef: WeakReference<Activity>

    fun getInstance(): MyActivityManager {
        return INSTANT
    }

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        if (sCurrentActivityWeakRef.get() != null) {
            currentActivity = sCurrentActivityWeakRef.get() as Activity
        }
        return currentActivity
    }

    fun setCurrentActivity(activity: Activity) {
        sCurrentActivityWeakRef = WeakReference<Activity>(activity)
    }
}