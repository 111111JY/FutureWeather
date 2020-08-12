package com.example.futureweather.logic.model

import android.location.Location
import com.google.gson.annotations.SerializedName

/**
 * 地区信息数据结构
 * @param status 返回状态
 * @param places 返回的地区数据列表
 */
data class PlaceResponse(val status: String, val places: List<Place>){
    /**
     * 地区
     * @param name 名称
     * @param location 位置信息
     * @param address 地址
     */
    data class Place(
        val name: String,
        val location: android.location.Location,
        @SerializedName("formatted_address") val address: String
    )

    /**
     * 位置信息
     * @param lng 经度
     * @param lat 纬度
     */
    data class Location(val lng: String, val lat: String)
}

