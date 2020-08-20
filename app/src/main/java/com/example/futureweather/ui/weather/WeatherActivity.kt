package com.example.futureweather.ui.weather

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.futureweather.R
import com.example.futureweather.extension.showToast
import com.example.futureweather.logic.model.*
import com.example.futureweather.utils.AppBarLayoutStateChangeListener
import com.example.futureweather.utils.GlobalUtil
import com.example.futureweather.utils.LogUtil
import com.example.futureweather.utils.broadcastreceiver.NetWorkStateReceiver
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.hourly.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.rain_2h.*
import kotlinx.android.synthetic.main.today_more.*
import java.util.*

class WeatherActivity : AppCompatActivity() {

    companion object {
        const val TAG = "WeatherActivity"
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }
    private var netWorkStateReceiver = NetWorkStateReceiver()
    private var currentTemperature: String = ""
    private var currentSkyText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        //初始化数据
        initData()
        //生成下拉刷新控件
        generateSwipeRefresh()
        //生成侧滑菜单
        generateDrawerLayout()
        //生成悬浮按钮
        generateFabBtn()
    }

    override fun onResume() {
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(netWorkStateReceiver, filter)
        LogUtil.i(TAG, "注册网络监听广播")
        super.onResume()
    }

    override fun onPause() {
        unregisterReceiver(netWorkStateReceiver)
        LogUtil.i(TAG, "注销网络监听广播")
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.addPlace -> drawerLayout.openDrawer(GravityCompat.END)
        }
        return true
    }

    /**
     * 刷新天气信息数据
     */
    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        toolbar.title = ""
        swipeRefresh.isRefreshing = true
        weatherContent.isSmoothScrollingEnabled = true
        weatherContent.smoothScrollTo(0, 0)
    }

    /**
     * 是否展示网络断开占位图
     * @param visibility Boolean
     */
    fun showDisconnectBg(visibility: Boolean) {
        if (visibility) {
            weatherContent.visibility = View.GONE
            disconnectHolderLayout.visibility = View.VISIBLE
        } else {
            disconnectHolderLayout.visibility = View.GONE
            weatherContent.visibility = View.VISIBLE
        }
    }

    /**
     * 设置标题栏
     */
    private fun setAppBar(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        appBar.addOnOffsetChangedListener(object : AppBarLayoutStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when (state) {
                    State.COLLAPSED -> {
                        toolbar.visibility = View.VISIBLE
                        collapsingToolbar.title = title
                    }
                    State.INTERMEDIATE -> {
                        toolbar.visibility = View.INVISIBLE
                        collapsingToolbar.title = ""
                    }
                    else -> {
                        toolbar.visibility = View.INVISIBLE
                        supportActionBar?.setDisplayShowTitleEnabled(false)
                        collapsingToolbar.title = ""
                    }
                }
            }
        })

    }

    /**
     * 初始化数据
     */
    private fun initData() {
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            LogUtil.i(TAG, result.toString())
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
                //设置标题栏
                setAppBar(viewModel.placeName + "  $currentSkyText  $currentTemperature")
            } else {
                "无法成功获取天气信息，请检查网络连接".showToast()
                setAppBar("定位失败")
                result.exceptionOrNull()?.printStackTrace()
            }
            //数据回来后，需要将进度条设置为停止
            swipeRefresh.isRefreshing = false
        })
    }

    /**
     * 展示天气信息
     * @param weather Weather 数据对象
     */
    private fun showWeatherInfo(weather: Weather) {
        val realtime = weather.realtime
        val daily = weather.daily
        val hourly = weather.hourly
        val minutely = weather.minutely
        //填充aqi.xml布局中的数据
        generateAqiUI(realtime)
        //填充today_more.xml布局中的数据
        generateTodayMore(realtime, daily)
        //填充rain_2h.xml布局中的数据
        generateMinutelyUI(minutely)
        //填充now.xml布局中的数据
        generateNowUI(realtime, daily)
        //填充hourly.xml布局中的数据
        generateHourlyUI(hourly)
        //填充forecast.xml布局中的数据
        generateForecastUI(daily)
        //填充life_index.xml布局中的数据
        generateLifeIndexUI(daily)
    }

    /**
     * 生成实时天气空气质量UI
     * @param realtime RealTime 数据对象
     */
    private fun generateAqiUI(realtime: RealTimeResponse.RealTime) {
        val descText = realtime.airQuality.description.chn
        //优、良、轻度污染、中度污染、重度污染
        if (descText.length == 1) {
            air_quality_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45F)
            air_quality_desc.text = descText
        } else {
            air_quality_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28F)
            air_quality_desc.text = descText
        }
        aqi_value.text = realtime.airQuality.aqi.chn.toInt().toString()
        pm25_value.text = realtime.airQuality.pm25.toString()
        when (realtime.airQuality.aqi.chn.toInt()) {
            in 0..50 -> airIconImg.setImageResource(R.drawable.ic_great)
            in 51..100 -> airIconImg.setImageResource(R.drawable.ic_normal)
            in 101..150 -> airIconImg.setImageResource(R.drawable.ic_bad1)
            in 151..200 -> airIconImg.setImageResource(R.drawable.ic_bad2)
            in 200..300 -> airIconImg.setImageResource(R.drawable.ic_bad3)
        }
    }

    /**
     * 生成今日天气详情UI
     * @param realtime RealTime 数据对象
     */
    private fun generateTodayMore(realtime: RealTimeResponse.RealTime, daily: DailyResponse.Daily) {
        minTempText.text = "${daily.temperature[0].min.toInt()}℃"
        maxTempText.text = "${daily.temperature[0].max.toInt()}℃"
        apparentTempText.text = "${realtime.apparent_temperature.toInt()}℃"
        humidityText.text = "${(realtime.humidity * 100).toInt()}%"
        sunriseText.text = "${daily.astro[0].sunrise.time}"
        sunsetText.text = "${daily.astro[0].sunset.time}"
        windDirectionText.text = Wind.getWindDirection(realtime.wind.direction)
        windStrengthText.text = Wind.getWindStrength(realtime.wind.speed)
    }

    /**
     * 生成实时天气UI
     * @param realtime RealTime 数据对象
     */
    private fun generateNowUI(realtime: RealTimeResponse.RealTime, daily: DailyResponse.Daily) {
        placeName.text = viewModel.placeName
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        serverTime.text = GlobalUtil.timeStampToString(calendar.timeInMillis, "MM月dd日  EEEE  HH:mm")
        title_skyIcon.setImageResource(getSky(realtime.skycon).icon)
        currentTemperature = "${realtime.temperature.toInt()}℃"
        currentTemp.text = currentTemperature
        currentSkyText = getSky(realtime.skycon).info
        comfortText.text = "今日天气人体感觉  ${daily.lifeIndex.comfort[0].desc}"
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
    }

    /**
     * 生成未来天气UI
     * @param daily Daily 数据对象
     */
    private fun generateForecastUI(daily: DailyResponse.Daily) {
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view =
                LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            dateInfo.text = GlobalUtil.timeStampToString(skycon.date.time, "MM-dd  EEEE")
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
    }

    /**
     * 生成分时天气UI
     * @param daily Daily 数据对象
     */
    private fun generateHourlyUI(hourly: HourlyResponse.Hourly) {
        hourlyLayout.removeAllViews()
        for (i in 0 until 10) {
            val skycon = getSky(hourly.skycon[i].value)
            val hourTimeText =
                GlobalUtil.timeStampToString(hourly.skycon[i].datetime.time, "MM-dd  HH:mm")
            val temperature = (hourly.temperature[i].value).toFloat().toInt()
            val windDirection = Wind.getWindDirection(hourly.wind[i].direction)

            val view =
                LayoutInflater.from(this).inflate(R.layout.hourly_item, hourlyLayout, false)
            val hourlyTime = view.findViewById(R.id.hourlyTime) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val tempInfo = view.findViewById(R.id.tempInfo) as TextView
            val windDirectionText = view.findViewById(R.id.windDirectionText) as TextView

            hourlyTime.text = hourTimeText
            skyIcon.setImageResource(skycon.icon)
            skyInfo.text = skycon.info
            tempInfo.text = "$temperature℃"
            windDirectionText.text = windDirection

            hourlyLayout.addView(view)
        }

        descriptionText.text = hourly.description
    }

    /**
     * 生成未来两小时内降雨概率UI
     * @param minutely Minutely 数据对象
     */
    private fun generateMinutelyUI(minutely: MinutelyResponse.Minutely) {
        rain30Text.text = "${(minutely.probability[0] * 100).toInt()}%"
        rain60Text.text = "${(minutely.probability[1] * 100).toInt()}%"
        rain90Text.text = "${(minutely.probability[2] * 100).toInt()}%"
        rain120Text.text = "${(minutely.probability[3] * 100).toInt()}%"
    }

    /**
     * 生成生活指数UI
     * @param daily Daily 数据对象
     */
    private fun generateLifeIndexUI(daily: DailyResponse.Daily) {
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        cardWashingText.text = lifeIndex.carWashing[0].desc
        weatherContent.visibility = View.VISIBLE
    }

    /**
     * 生成侧滑菜单栏UI(切换城市fragment @PlaceFragment)
     */
    private fun generateDrawerLayout() {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            override fun onDrawerOpened(drawerView: View) {}
        })
    }

    /**
     * 生成下拉刷新控件
     */
    private fun generateSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
    }

    /**
     * 生成悬浮按钮
     */
    private fun generateFabBtn() {
        //下滑隐藏悬浮按钮  上滑显示悬浮按钮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            weatherContent.setOnScrollChangeListener { scrollView, x, y, oldx, oldy ->
                if (y > oldy + 50) {
                    fabShareBtn.visibility = View.INVISIBLE
                }
                if (y < oldy - 50) {
                    fabShareBtn.visibility = View.VISIBLE
                }
            }
        }

        fabShareBtn.setOnClickListener {
            "分享功能正在加速开发中，请耐心等待！".showToast()
        }

        fabControlPlaceBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        fabAddPlaceBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }

}
