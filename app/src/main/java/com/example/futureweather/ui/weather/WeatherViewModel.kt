package com.example.futureweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.futureweather.logic.Repository
import com.example.futureweather.logic.model.PlaceResponse

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<PlaceResponse.Location>()

    var locaitonLng = ""
    var locationLat = ""
    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = PlaceResponse.Location(lng, lat)
    }
}
