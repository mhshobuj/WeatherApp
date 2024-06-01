package com.mhs.weatherapp.repository

import com.mhs.weatherapp.api.ApiService
import com.mhs.weatherapp.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository@Inject constructor(private val apiService: ApiService) {

    suspend fun getCityWeatherList(lat: Double, lon: Double, cnt: Int, appid: String) = flow {
        emit(DataStatus.loading())
        val result = apiService.getCityWeatherList(lat, lon, cnt, appid)
        when (result.code()) {
            200 -> {
                emit(DataStatus.success(result.body()))
            }
            400 -> {
                emit(DataStatus.error(result.message()))
            }
            500 -> {
                emit(DataStatus.error(result.message()))
            }
            else -> {
                emit(DataStatus.error("Unexpected error: ${result.message()}"))
            }
        }
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun getCityWeatherDetails(cityName: String, appid: String) = flow {
        emit(DataStatus.loading())
        val result = apiService.getCityWeatherDetails(cityName, appid)
        when (result.code()) {
            200 -> {
                emit(DataStatus.success(result.body()))
            }
            400 -> {
                emit(DataStatus.error(result.message()))
            }
            500 -> {
                emit(DataStatus.error(result.message()))
            }
            else -> {
                emit(DataStatus.error("Unexpected error: ${result.message()}"))
            }
        }
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}