package com.example.futureweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 未来天气信息数据结构
 */
data class DailyResponse(val status: String, val result: Result) {

    data class Result(val daily: Daily)

    data class Daily(
        val temperature: List<Temperature>,
        val skycon: List<SkyCon>,
        val astro: List<Astro>,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    )

    data class Temperature(val max: Float, val min: Float)

    data class SkyCon(val value: String, val date: Date)

    data class Astro(val sunrise: SunRise, val sunset: SunSet)

    data class SunRise(val time: String)

    data class SunSet(val time: String)

    data class LifeIndex(
        val coldRisk: List<LifeDescription>,
        val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>,
        val dressing: List<LifeDescription>,
        val comfort: List<LifeDescription>
    )

    data class LifeDescription(val desc: String)
}