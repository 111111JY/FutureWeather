package com.example.futureweather.ui.weather

import android.content.Context
import android.os.Build
import android.os.Bundle
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
import com.example.futureweather.logic.model.DailyResponse
import com.example.futureweather.logic.model.RealTimeResponse
import com.example.futureweather.logic.model.Weather
import com.example.futureweather.logic.model.getSky
import com.example.futureweather.utils.AppBarLayoutStateChangeListener
import com.example.futureweather.utils.GlobalUtil
import com.example.futureweather.utils.LogUtil
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*

class WeatherActivity : AppCompatActivity() {

    companion object {
        const val TAG = "WeatherActivity"
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }
    private var currentTemperature: String = ""
    private var currentSkyText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //将状态栏设置为透明色
//        GlobalUtil.setStatusBarTransparent(window)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> "管理地区功能正在加速开发中，请耐心等待！".showToast()
            R.id.addPlace -> drawerLayout.openDrawer(GravityCompat.END)
        }
        return true
    }

    /**
     * 刷新天气信息数据
     */
    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        swipeRefresh.isRefreshing = true

        weatherContent.isSmoothScrollingEnabled = true
        weatherContent.smoothScrollTo(0, 0)
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
                "无法成功获取天气信息".showToast()
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
        //填充now.xml布局中的数据
        generateNowUI(realtime)
        //填充forecast.xml布局中的数据
        generateForecastUI(daily)
        //填充life_index.xml布局中的数据
        generateLifeIndexUI(daily)
    }

    /**
     * 生成实时天气UI
     * @param realtime RealTime 数据对象
     */
    private fun generateNowUI(realtime: RealTimeResponse.RealTime) {
        placeName.text = viewModel.placeName
        currentTemperature = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTemperature
        currentSkyText = getSky(realtime.skycon).info
        currentSky.text = currentSkyText
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
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
//            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            dateInfo.text = simpleDateFormat.format(skycon.date)
//            dateInfo.text = GlobalUtil.dateToString(skycon.date, "yyyy-MM-dd")
            dateInfo.text = GlobalUtil.timeStampToString(skycon.date.time, "yyyy-MM-dd")
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
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
        //进入时刷新一次数据，确保展示最新的数据
        refreshWeather()
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
            "管理地区功能正在加速开发中，请耐心等待！".showToast()
        }

        fabAddPlaceBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }
}
