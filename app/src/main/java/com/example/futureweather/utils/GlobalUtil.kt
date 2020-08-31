package com.example.futureweather.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import com.example.futureweather.FutureWeatherApplication
import com.example.futureweather.R
import com.example.futureweather.widget.DiyDialog
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.CHINA

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

    /**
     * 将时间戳转化为日期字符串
     * yyyy-MM-dd EEEE 2020-08-15 Saturday
     * hh:mm a
     * @param timestamp Long 时间戳
     * @param formatType String 需要转化为的日期格式
     * @return String 日期字符串
     */
    fun timeStampToString(timestamp: Long, formatType: String): String =
        SimpleDateFormat(formatType, CHINA).format(timestamp)

    /**
     * 将日期字符串转化为时间戳
     * @param time String 日期字符串
     * @param formatType String 需要转化的日期格式
     * @return Long 时间戳
     */
    fun stringToTimeStamp(time: String, formatType: String): Long =
        SimpleDateFormat(formatType, CHINA).parse(
            time,
            ParsePosition(0)
        )!!.time

    /**
     * 将日期数据转化为日期字符串
     * yyyy-MM-dd EEEE 2020-08-15 Saturday
     * hh:mm a
     * @param date Date 日期数据
     * @param formatType String 需要转化为的日期格式
     * @return String 日期字符串
     */
    fun dateToString(date: Date, formatType: String): String =
        SimpleDateFormat(formatType, CHINA).format(date)

    /**
     * 展示提示性弹窗
     * @param context Context
     * @param content String
     * @param confirmText String
     */
    fun showTipDialog(
        context: Context,
        content: String,
        confirmText: String
    ) {
        val diyDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_tip_layout, null)
        val confirmBtn = diyDialogView.findViewById<TextView>(R.id.confirm_button)
        val contentText = diyDialogView.findViewById<TextView>(R.id.content_text)
        confirmBtn.text = confirmText
        contentText.text = content
        val dialog = DiyDialog(context, R.style.DiyDialog, diyDialogView)
        confirmBtn.setOnClickListener {
            dialog.closeDiyDialog()
        }
        dialog.showDiyDialog()
    }
}

