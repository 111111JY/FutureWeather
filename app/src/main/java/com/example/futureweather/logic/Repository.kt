package com.example.futureweather.logic

import androidx.lifecycle.liveData
import com.example.futureweather.logic.dao.PlaceDao
import com.example.futureweather.logic.model.PlaceResponse
import com.example.futureweather.logic.model.Weather
import com.example.futureweather.logic.network.FutureWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层的统一封装入口
 */
object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = FutureWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                FutureWeatherNetwork.getRealTime(lng, lat)
            }
            val deferredDaily = async {
                FutureWeatherNetwork.getDaily(lng, lat)
            }

            val realTimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()

            if (realTimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realTimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realTimeResponse status is ${realTimeResponse.status}" +
                                "dailyResponse status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    fun savePlace(place: PlaceResponse.Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() =  PlaceDao.getSavedPlace()

    fun isPlaceSaved() =  PlaceDao.isPlaceSaved()

    /**
     * 对返回的结果统一做try catch处理
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}
