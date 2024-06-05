package com.mhs.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhs.weatherapp.model.CityWeatherDetailsResponse
import com.mhs.weatherapp.model.CityWeatherListResponse
import com.mhs.weatherapp.repository.MainRepository
import com.mhs.weatherapp.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    // LiveData for city weather list
    private val _cityWeatherList = MutableLiveData<DataStatus<CityWeatherListResponse>>()
    val cityWeatherList: LiveData<DataStatus<CityWeatherListResponse>> get() = _cityWeatherList

    fun getCityWeatherList(lat: Double, lon: Double, cnt: Int, appid: String) = viewModelScope.launch {
        mainRepository.getCityWeatherList(lat, lon, cnt, appid).collect {
            _cityWeatherList.value = it
        }
    }

    // LiveData for city weather details
    private val _cityWeatherDetails = MutableLiveData<DataStatus<CityWeatherDetailsResponse>>()
    val cityWeatherDetails: LiveData<DataStatus<CityWeatherDetailsResponse>> get() = _cityWeatherDetails

    fun getCityWeatherDetails(cityName: String, appid: String) = viewModelScope.launch {
        mainRepository.getCityWeatherDetails(cityName, appid).collect {
            _cityWeatherDetails.value = it
        }
    }

    // LiveData for city weather details by lat lng
    private val _latLngWeatherDetails = MutableLiveData<DataStatus<CityWeatherDetailsResponse>>()
    val latLngWeatherDetails: LiveData<DataStatus<CityWeatherDetailsResponse>> get() = _latLngWeatherDetails

    fun getWeatherByLatLng(lat: Double,lng: Double, appid: String) = viewModelScope.launch {
        mainRepository.getWeatherByLatLng(lat, lng, appid).collect {
            _latLngWeatherDetails.value = it
        }
    }
}