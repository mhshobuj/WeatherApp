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

    /**
     * Fetches a list of city weather data from the repository.
     *
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     * @param cnt Number of cities to fetch.
     * @param appid API key for authentication.
     */
    fun getCityWeatherList(lat: Double, lon: Double, cnt: Int, appid: String) = viewModelScope.launch {
        mainRepository.getCityWeatherList(lat, lon, cnt, appid).collect {
            _cityWeatherList.value = it
        }
    }

    // LiveData for city weather details
    private val _cityWeatherDetails = MutableLiveData<DataStatus<CityWeatherDetailsResponse>>()
    val cityWeatherDetails: LiveData<DataStatus<CityWeatherDetailsResponse>> get() = _cityWeatherDetails

    /**
     * Fetches a city weather details data from the repository.
     *
     * @param cityName name of city
     * @param appid API key for authentication.
     */
    fun getCityWeatherDetails(cityName: String, appid: String) = viewModelScope.launch {
        mainRepository.getCityWeatherDetails(cityName, appid).collect {
            _cityWeatherDetails.value = it
        }
    }
}