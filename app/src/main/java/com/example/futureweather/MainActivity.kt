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
        LogUtil.i(WeatherActivity.TAG, getString(R.string.registerBroadcast))
        super.onResume()
    }

    override fun onPause() {
        unregisterReceiver(netWorkStateReceiver)
        LogUtil.i(WeatherActivity.TAG, getString(R.string.unregisterBroadcast))
        super.onPause()
    }

    override fun onStop() {
        //暂停动画  避免内存溢出
        animation_place_footer.pauseAnimation()
        super.onStop()
    }

    /**
     * 是否展示断开网络展位图
     * @param show Boolean
     */
    fun showDisconnectHolderBg(show: Boolean) {
        if (show) {
            animation_place_footer.setAnimation(R.raw.disconnected_boy)
            tipsText.text = getString(R.string.disconnect_tips)
        } else {
            animation_place_footer.setAnimation(R.raw.place_search_footer_bus)
            tipsText.text = getString(R.string.tips_text)
        }
        animation_place_footer.resumeAnimation()
    }
}
