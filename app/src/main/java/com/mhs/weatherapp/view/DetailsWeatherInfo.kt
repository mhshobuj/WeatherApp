package com.mhs.weatherapp.view

import Constants
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mhs.weatherapp.R
import com.mhs.weatherapp.databinding.ActivityDetailsWeatherInfoBinding
import com.mhs.weatherapp.utils.DataStatus
import com.mhs.weatherapp.utils.NetworkChecking
import com.mhs.weatherapp.utils.StatusBarUtils
import com.mhs.weatherapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import isVisible
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.ln
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailsWeatherInfo : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailsWeatherInfoBinding
    private val viewModel: MainViewModel by viewModels()
    private var connectivityStatus: String? = null
    private lateinit var mMap: GoogleMap
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize view binding
        binding = ActivityDetailsWeatherInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set the status bar color
        StatusBarUtils.setStatusBarColor(this, R.color.STATUS_BAR_COLOR, lightStatusBar = true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_live) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Network checking
        val networkChecking = NetworkChecking
        connectivityStatus = networkChecking.getConnectivityStatusString(this)

        binding.toolbarLayout.ivBack.setOnClickListener {
            finish()
        }

        // Retrieve the value for "CITY_NAME" extra from the Intent
        val cityName = intent.getStringExtra("CITY_NAME")
        Log.e("Details", "" + cityName)

        if (cityName != null) {
            GlobalScope.launch {
                getCityWeatherDetails(cityName)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCityWeatherDetails(cityName: String) {
        if (connectivityStatus == "Wifi enabled" || connectivityStatus == "Mobile data enabled") {
            lifecycleScope.launch {
                binding.apply {
                    viewModel.getCityWeatherDetails(cityName, Constants.APP_ID)
                    viewModel.cityWeatherDetails.observe(this@DetailsWeatherInfo) {
                        when (it.status) {
                            DataStatus.Status.LOADING -> {
                                pBarLoading.isVisible(true, mainLayout)
                            }

                            DataStatus.Status.SUCCESS -> {
                                pBarLoading.isVisible(false, mainLayout)
                                Log.e("Details", "" + it.data!!.base)
                                tvNameCity.text = it.data.name
                                tvDescription.text = it.data.weather[0].main
                                tvHumidity.text = "Humidity: ${it.data.main.humidity}"
                                tvWindSpeed.text = "Wind Speed: ${it.data.wind.speed}"

                                // Convert temperatures from Kelvin to Celsius
                                val mainTemp: Double = it.data.main.temp
                                val celsiusTemp: Double = (mainTemp - 273.15)
                                val maxTemp: Double = it.data.main.tempMax
                                val celsiusMaxTemp: Double = (maxTemp - 273.15)
                                val minTemp: Double = it.data.main.tempMin
                                val celsiusMinTemp: Double = (minTemp - 273.15)

                                // Update text views with the converted values
                                tvMaxTemp.text = "Max. Temp: ${celsiusMaxTemp.roundToInt()}°c"
                                tvMinTemp.text = "Min. Temp: ${celsiusMinTemp.roundToInt()}°c"
                                tvTemp.text = "${celsiusTemp.roundToInt()}°c"

                                // Update latitude and longitude
                                lat = it.data.coord.lat
                                lng = it.data.coord.lon

                                // Update the map with the new coordinates
                                updateMap()
                            }

                            DataStatus.Status.ERROR -> {
                                pBarLoading.isVisible(false, mainLayout)
                                Toast.makeText(
                                    this@DetailsWeatherInfo,
                                    "There is something wrong!!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        } else{
            Toast.makeText(
                this@DetailsWeatherInfo,
                "Internet not available!!",
                Toast.LENGTH_LONG
            ).show()
        }

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateMap()
    }

    private fun updateMap() {
        if (::mMap.isInitialized && lat != 0.0 && lng != 0.0) {
            val myCity = LatLng(lat, lng)
            mMap.addMarker(MarkerOptions().position(myCity).title("My City"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCity, 17.0f)) // Adjust zoom level as needed
        }
    }

}