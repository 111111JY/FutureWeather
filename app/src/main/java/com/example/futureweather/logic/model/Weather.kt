package com.example.futureweather.logic.model

/**
 * 封装RealTime和Daily对象
 */
data class Weather(val realtime: RealTimeResponse.RealTime, val daily: DailyResponse.Daily)