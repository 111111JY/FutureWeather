package com.example.futureweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.futureweather.FutureWeatherApplication
import com.example.futureweather.logic.model.PlaceResponse
import com.google.gson.Gson

object PlaceDao {

    fun savePlace(place: PlaceResponse.Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): PlaceResponse.Place {
        val placeJson = sharedPreferences()
            .getString("place", "")
        return Gson().fromJson(placeJson, PlaceResponse.Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = FutureWeatherApplication.context.getSharedPreferences(
        "future_weather",
        Context.MODE_PRIVATE
    )
}