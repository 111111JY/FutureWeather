package com.example.futureweather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 实时天气信息数据结构
 */
data class RealTimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: RealTime)

    data class RealTime(
        val skycon: String,
        val temperature: Float,
        val apparent_temperature: Float,
        val humidity: Float,
        val wind: Wind,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class Wind(val direction: Float, val speed: Float)

    data class AirQuality(val pm25: Int, val aqi: AQI, val description: Description)

    data class AQI(val chn: Float)

    data class Description(val chn: String)
}