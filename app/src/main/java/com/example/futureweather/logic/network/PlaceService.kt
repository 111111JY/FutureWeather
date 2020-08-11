package com.example.futureweather.logic.network

import com.example.futureweather.FutureWeatherApplication
import com.example.futureweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("v2/place?token=${FutureWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}