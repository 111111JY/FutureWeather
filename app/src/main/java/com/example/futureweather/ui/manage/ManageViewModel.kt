package com.example.futureweather.ui.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.futureweather.logic.Repository
import com.example.futureweather.logic.model.ManagePlace
import com.example.futureweather.logic.model.PlaceResponse

class ManageViewModel : ViewModel() {

    //saveLiveData
    fun savePlace(placeList: ArrayList<PlaceResponse.Place>) = Repository.savePlace(placeList)

    fun getSavedLiveData() = Repository.getSavedLocationPlace()

    fun getManagePlaceList() = Repository.getSavedManagePlace()

    fun isPlaceSaved(place:PlaceResponse.Place) = Repository.isPlaceSaved(place)

    fun hasPlace() = Repository.hasPlace()
}