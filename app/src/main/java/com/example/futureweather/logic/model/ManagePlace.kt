package com.example.futureweather.logic.model

data class ManagePlace(
    val place: PlaceResponse.Place,
    val weatherInfo: RealTimeResponse.RealTime
)