package com.example.futureweather

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.futureweather.ui.weather.WeatherActivity
import com.example.futureweather.utils.LogUtil
import com.example.futureweather.utils.broadcastreceiver.NetWorkStateReceiver
import kotlinx.android.synthetic.main.fragment_place.*

class MainActivity : AppCompatActivity() {
    private var netWorkStateReceiver = NetWorkStateReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(netWorkStateReceiver, filter)
        LogUtil.i(WeatherActivity.TAG, "注册网络监听广播")
        super.onResume()
    }

    override fun onPause() {
        unregisterReceiver(netWorkStateReceiver)
        LogUtil.i(WeatherActivity.TAG, "注销网络监听广播")
        super.onPause()
    }

    /**
     * 是否展示断开网络展位图
     * @param show Boolean
     */
    fun showDisconnectHolderBg(show: Boolean) {
        if (show) {
            bgImageView.setImageResource(R.drawable.bg_disconnect_girl)
            tipsText.text = getString(R.string.disconnect_tips)
        } else {
            bgImageView.setImageResource(R.drawable.bg_choose_place)
            tipsText.text = getString(R.string.tips_text)
        }
    }
}
