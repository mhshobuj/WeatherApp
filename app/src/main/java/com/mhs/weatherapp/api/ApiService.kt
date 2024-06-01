package com.mhs.weatherapp.api
import com.mhs.weatherapp.model.CityWeatherDetailsResponse
import com.mhs.weatherapp.model.CityWeatherListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Get a list of city weather
    @GET("data/2.5/find")
    suspend fun getCityWeatherList(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int,
        @Query("appid") appid: String
    ): Response<CityWeatherListResponse>

    // Get details of a specific city
    @GET("data/2.5/weather")
    suspend fun getCityWeatherDetails(
        @Query("q") city_name: String,
        @Query("appid") appid: String
    ): Response<CityWeatherDetailsResponse>
}