package com.example.futureweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 实时天气信息数据结构
 */
data class HourlyResponse(val status: String, val result: Result) {

    data class Result(val hourly: Hourly)

    data class Hourly(
        val description: String,
        @SerializedName("skycon") val skycon: List<SkyCon>,
        @SerializedName("temperature") val temperature: List<Temperature>,
        @SerializedName("wind") val wind: List<Wind>
    )

    data class SkyCon(val datetime: Date, val value: String)

    data class Temperature(val datetime: Date, val value: String)

    data class Wind(val datetime: Date, val direction: Float, val speed: Float)
}