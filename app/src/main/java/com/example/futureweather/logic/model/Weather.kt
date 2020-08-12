package com.example.futureweather.logic.model

/**
 * 封装RealTime和Daily对象
 */
data class Weather(val realTime: RealTimeResponse.RealTime, val daily: DailyResponse.Daily)