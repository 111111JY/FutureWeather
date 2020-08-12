package com.example.futureweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 封装网络请求API
 */
object FutureWeatherNetwork {

    private val httpService = ServiceCreator.create<HttpService>()
    /**
     * 发起查询地区信息请求
     */
    suspend fun searchPlaces(query: String) = httpService.searchPlaces(query).await()

    /**
     * 发起查询某个地区的实时天气信息请求
     */
    suspend fun getRealTime(lng: String, lat: String) = httpService.getRealTimeWeather(lng, lat).await()

    /**
     * 发起查询某个地区的未来天气信息请求
     */
    suspend fun getDaily(lng: String, lat: String) = httpService.getDailyWeather(lng, lat).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null !"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}