package com.example.futureweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.futureweather.logic.Repository
import com.example.futureweather.logic.model.PlaceResponse

class PlaceViewModel : ViewModel() {
    val placeList = ArrayList<PlaceResponse.Place>()

    // searchLiveData
    private val searchLiveData = MutableLiveData<String>()
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    //saveLiveData

    fun savePlace(place: PlaceResponse.Place) = Repository.savePlace(place)

    fun getSavedLiveData() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}