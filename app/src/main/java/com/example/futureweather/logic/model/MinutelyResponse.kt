package com.example.futureweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 两小时内降雨概率天气信息数据结构
 */
data class MinutelyResponse(val status: String, val result: Result) {

    data class Result(val minutely: Minutely)

//    data class Minutely(
//        val description: String,
//        @SerializedName("probability") val probability: List<Probability>
//    )
//
//    data class Probability(val value: Float)

    data class Minutely(
        val description: String,
        val probability: MutableList<Float>
    )

}