package com.example.futureweather.logic

import androidx.lifecycle.liveData
import com.example.futureweather.logic.model.Place
import com.example.futureweather.logic.network.FutureWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库层的统一封装入口
 */
object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = FutureWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}