package com.example.futureweather.utils

import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import android.view.Window
import com.example.futureweather.FutureWeatherApplication

/**
 * 全局通用工具
 */
object GlobalUtil {
    /**
     * 判断当前系统是否为黑夜模式
     */
    fun isDarkTheme(): Boolean {
        val flag =
            FutureWeatherApplication.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * 将状态栏设置为透明色
     * 该方法必须设置在 setContentView(R.layout.xxx) 方法前才会生效
     * @param window Window
     */
    fun setStatusBarTransparent(window: Window) {
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
    }
}