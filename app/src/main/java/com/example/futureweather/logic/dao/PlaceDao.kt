package com.example.futureweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.futureweather.FutureWeatherApplication
import com.example.futureweather.logic.model.PlaceResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)

object PlaceDao {

    fun savePlace(placeList: ArrayList<PlaceResponse.Place>) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(placeList))
        }
    }

    fun getSavedLocationPlace(): PlaceResponse.Place {
        val placeJson = sharedPreferences()
            .getString("place", "")
        val placeList: ArrayList<PlaceResponse.Place> =
            Gson().fromJson<ArrayList<PlaceResponse.Place>>(
                placeJson.toString()
            )
        return placeList[0]
    }

    fun getSavedManagePlace(): ArrayList<PlaceResponse.Place> {
        val placeJson = sharedPreferences()
            .getString("place", "")
        return if (placeJson.isNullOrEmpty()){
            ArrayList<PlaceResponse.Place>()
        } else {
            Gson().fromJson(
                placeJson.toString()
            )
        }
    }

    fun isPlaceSaved(place: PlaceResponse.Place): Boolean {
        return if (hasPlace()) {
            sharedPreferences().getString("place", "")!!.contains(Gson().toJson(place))
        } else {
            false
        }
    }

    fun hasPlace() = sharedPreferences().contains("place")

    private fun sharedPreferences() = FutureWeatherApplication.context.getSharedPreferences(
        "future_weather",
        Context.MODE_PRIVATE
    )
}