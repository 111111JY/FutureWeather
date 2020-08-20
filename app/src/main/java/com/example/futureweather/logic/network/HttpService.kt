package com.example.futureweather.logic.network

import com.example.futureweather.FutureWeatherApplication
import com.example.futureweather.logic.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 访问网络数据的统一入口
 */
interface HttpService {

    /**
     * 请求地区信息
     */
    @GET("v2/place?token=${FutureWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

    /**
     * 请求某个地区的实时天气信息
     */
    @GET("v2.5/${FutureWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealTimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealTimeResponse>

    /**
     * 请求某个地区的两小时内下雨概率天气信息
     */
    @GET("v2.5/${FutureWeatherApplication.TOKEN}/{lng},{lat}/minutely.json")
    fun getMinutelyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<MinutelyResponse>

    /**
     * 请求某个地区的时级天气信息
     */
    @GET("v2.5/${FutureWeatherApplication.TOKEN}/{lng},{lat}/hourly.json")
    fun getHourlyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<HourlyResponse>

    /**
     * 请求某个地区的未来天气信息
     */
    @GET("v2.5/${FutureWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}