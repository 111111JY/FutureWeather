package com.example.futureweather.logic

import androidx.lifecycle.liveData
import com.example.futureweather.extension.showToast
import com.example.futureweather.logic.dao.PlaceDao
import com.example.futureweather.logic.model.PlaceResponse
import com.example.futureweather.logic.model.Weather
import com.example.futureweather.logic.network.FutureWeatherNetwork
import com.example.futureweather.utils.LogUtil
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

            val deferredHourly = async {
                FutureWeatherNetwork.getHourly(lng, lat)
            }

            val deferredMinutely = async {
                FutureWeatherNetwork.getMinutely(lng, lat)
            }

            LogUtil.i(
                "ApiTest",
                "realTime api url= https://api.caiyunapp.com/v2.5/TH9Qw0cSKnY98jQG/$lng,$lat/realtime.json"
            )
            LogUtil.i(
                "ApiTest",
                "daily api url= https://api.caiyunapp.com/v2.5/TH9Qw0cSKnY98jQG/$lng,$lat/hourly.json"
            )
            LogUtil.i(
                "ApiTest",
                "daily api url= https://api.caiyunapp.com/v2.5/TH9Qw0cSKnY98jQG/$lng,$lat/daily.json"
            )
            LogUtil.i(
                "ApiTest",
                "daily api url= https://api.caiyunapp.com/v2.5/TH9Qw0cSKnY98jQG/$lng,$lat/minutely.json"
            )

            val realTimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            val hourlyResponse = deferredHourly.await()
            val minutelyResponse = deferredMinutely.await()

            if (realTimeResponse.status == "ok" &&
                dailyResponse.status == "ok" &&
                hourlyResponse.status == "ok" &&
                minutelyResponse.status == "ok"
            ) {
                val weather =
                    Weather(
                        realTimeResponse.result.realtime,
                        dailyResponse.result.daily,
                        hourlyResponse.result.hourly,
                        minutelyResponse.result.minutely
                    )
                "数据更新成功".showToast()
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realTimeResponse status is ${realTimeResponse.status}" +
                                "dailyResponse status is ${dailyResponse.status}" +
                                "hourlyResponse status is ${hourlyResponse.status}" +
                                "minutelyResponse status is ${minutelyResponse.status}"
                    )
                )
            }
        }
    }

    fun savePlace(place: PlaceResponse.Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

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
