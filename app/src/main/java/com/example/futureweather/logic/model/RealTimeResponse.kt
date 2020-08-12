package com.example.futureweather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 实时天气信息数据结构
 */
data class RealTimeResponse(val status: String, val result: Result) {
    data class Result(val realTime: RealTime)

    data class RealTime(
        val skycon: String,
        val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)
}