package com.example.futureweather.widget

import android.R
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Parcel
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.StyleRes

/**
 * 自定义Dialog
 * Created by 邹峰立 on 2017/7/6.
 */
class DiyDialog(private val context: Context, @StyleRes themeResId: Int, view: View) : Parcelable {
    private val dialog: Dialog = Dialog(context, themeResId)

    enum class DiyDialogGravity {
        GRAVITY_CENTER,
        GRAVITY_LEFT,
        GRAVITY_RIGHT,
        GRAVITY_TOP,
        GRAVITY_BOTTOM
    }


    /**
     * 初始化控件
     */
    private fun init(view: View) {
        dialog.setContentView(view)
        // 按返回键是否取消
        dialog.setCancelable(true)
        // 点击Dialog外围是否取消
        dialog.setCanceledOnTouchOutside(true)
        // 设置默认透明度0.2f
        setDimAmount(0.2f)
        // 设置默认居中显示
        setDiyDialogGravity(DiyDialogGravity.GRAVITY_CENTER)
    }

    /**
     * 设置Dialog显示内容
     *
     * @param view 内容View
     */
    private fun setContentView(view: View?) {
        if (view != null) dialog.setContentView(view)
    }

    /**
     * 按返回键是否取消
     *
     * @param cancelable true 取消 false 不取消  默认true
     */
    fun setCancelable(cancelable: Boolean): DiyDialog {
        dialog.setCancelable(cancelable)
        return this
    }

    /**
     * 点击Dialog外围是否取消
     *
     * @param cancelable true 取消 false 不取消  默认false
     */
    fun setCanceledOnTouchOutside(cancelable: Boolean): DiyDialog {
        dialog.setCanceledOnTouchOutside(cancelable)
        return this
    }

    /**
     * 设置取消事件
     *
     * @param onCancelListener 取消事件
     */
    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?): DiyDialog {
        dialog.setOnCancelListener(onCancelListener)
        return this
    }

    /**
     * 设置Dialog宽度
     *
     * @param proportion 和屏幕的宽度比(10代表10%) 0~100
     */
    fun setDiyDialogWidth(proportion: Int): DiyDialog {
        val window = dialog.window
        if (window != null) {
            val lp: WindowManager.LayoutParams = window.getAttributes()
            lp.width = getScreenW(context) * proportion / 100
            window.attributes = lp
        }
        return this
    }

    /**
     * 设置Dialog高度
     *
     * @param proportion 和屏幕的高度比(10代表10%) 0~100
     */
    fun setDiyDialogHeight(proportion: Int): DiyDialog {
        val window= dialog.window
        if (window != null) {
            val lp: WindowManager.LayoutParams = window.attributes
            lp.height = getScreenH(context) * proportion / 100
            window.attributes = lp
        }
        return this
    }

    /**
     * 设置Dialog显示位置
     *
     * @param diyDialogGravity 左上右下中
     */
    fun setDiyDialogGravity(diyDialogGravity: DiyDialogGravity): DiyDialog {
        val window= dialog.window
        var gravity = Gravity.CENTER
        when (diyDialogGravity) {
            DiyDialogGravity.GRAVITY_BOTTOM -> {
                gravity = Gravity.BOTTOM
            }
            DiyDialogGravity.GRAVITY_CENTER -> {
                gravity = Gravity.CENTER
            }
            DiyDialogGravity.GRAVITY_LEFT -> {
                gravity = Gravity.START
            }
            DiyDialogGravity.GRAVITY_RIGHT -> {
                gravity = Gravity.END
            }
            DiyDialogGravity.GRAVITY_TOP -> {
                gravity = Gravity.TOP
            }
        }
        if (window != null) window.attributes.gravity = gravity
        return this
    }

    /**
     * 设置背景层透明度
     *
     * @param dimAmount 0~1
     */
    fun setDimAmount(dimAmount: Float): DiyDialog {
        val window= dialog.window
        if (window != null) {
            val lp: WindowManager.LayoutParams = window.attributes
            // 设置背景层透明度
            lp.dimAmount = dimAmount
            window.attributes = lp
        }
        return this
    }

    /**
     * 设置Window动画
     *
     * @param style R文件
     */
    fun setWindowAnimations(style: Int): DiyDialog {
        val window= dialog.window
        window?.setWindowAnimations(style)
        return this
    }

    /**
     * 展示Dialog
     */
    fun showDiyDialog() {
        dialog.show()
    }

    /**
     * 关闭Dialog
     */
    fun closeDiyDialog() {
        dialog.cancel()
    }

    /**
     * 获取屏幕宽度
     */
    private fun getScreenW(context: Context): Int {
        val dm: DisplayMetrics = context.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * 获取屏幕的高度
     */
    private fun getScreenH(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    init {
        init(view)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

//    companion object CREATOR : Parcelable.Creator<DiyDialog> {
//        override fun createFromParcel(parcel: Parcel): DiyDialog {
//            return DiyDialog(parcel)
//        }
//
//        override fun newArray(size: Int): Array<DiyDialog?> {
//            return arrayOfNulls(size)
//        }
//    }
    companion object CREATOR : Parcelable.Creator<DiyDialog> {
        override fun createFromParcel(parcel: Parcel): DiyDialog {
            return DiyDialog(parcel)
        }

    private fun DiyDialog(parcel: Parcel): DiyDialog {
        return DiyDialog(parcel)
    }

    override fun newArray(size: Int): Array<DiyDialog?> {
            return arrayOfNulls(size)
        }
    }
}