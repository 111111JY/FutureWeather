package com.example.futureweather.utils.broadcastreceiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.futureweather.MainActivity
import com.example.futureweather.MyActivityManager
import com.example.futureweather.extension.showToast
import com.example.futureweather.ui.weather.WeatherActivity

class NetWorkStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        //获取当前Activity的实例
        val activity = MyActivityManager.INSTANT.getCurrentActivity()
        //获得ConnectivityManager对象
        val connectionManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            val wifiNetWorkInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            //获取移动数据连接的信息
            val dateNetWorkInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (wifiNetWorkInfo!!.isConnected || dateNetWorkInfo!!.isConnected) {
                hasConnectionOperation(activity)
            } else {
                noConnectionOperation(activity)
            } //API大于23时使用下面的方式进行网络监听
        } else {
            //获取所有网络连接的信息
            val netWorks = connectionManager.allNetworkInfo
            var hasConnection = false
            //通过循环将网络信息逐个取出来
            for (i in netWorks) {
                if (i!!.isConnected) {
                    hasConnection = true
                    break
                } else {
                    hasConnection = false
                }
            }
            if (hasConnection) {
                hasConnectionOperation(activity)
            } else {
                noConnectionOperation(activity)
            }
        }
    }

    /**
     * 网络已连接时的操作
     * @param activity Activity?
     */
    private fun hasConnectionOperation(activity: Activity?) {
        if (activity != null) {
            if (activity is WeatherActivity) {
                activity.showDisconnectBg(false)
                activity.refreshWeather()
            }
            if (activity is MainActivity) {
                activity.showDisconnectHolderBg(false)
            }
        }
    }

    /**
     * 网络已断开的操作
     * @param activity Activity?
     */
    private fun noConnectionOperation(activity: Activity?) {
        if (activity != null) {
            if (activity is WeatherActivity) {
                activity.showDisconnectBg(true)
                "网络已断开，请连接网络".showToast()
            }
            if (activity is MainActivity) {
                activity.showDisconnectHolderBg(true)
            }
        }
    }
}