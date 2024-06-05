package com.mhs.weatherapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.Calendar
import com.mhs.weatherapp.utils.DataStatus
import com.mhs.weatherapp.utils.NetworkChecking
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mhs.weatherapp.R
import com.mhs.weatherapp.adapter.CityWeatherAdapter
import com.mhs.weatherapp.databinding.ActivityMainBinding
import com.mhs.weatherapp.utils.Constants
import com.mhs.weatherapp.utils.NotificationWorker
import com.mhs.weatherapp.utils.StatusBarUtils
import com.mhs.weatherapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import initRecycler
import isVisible
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var cityWeatherAdapter: CityWeatherAdapter? = null
    private var connectivityStatus: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        binding.toolbarLayout.ivBack.visibility = View.GONE

        // Set the status bar color
        StatusBarUtils.setStatusBarColor(this, R.color.STATUS_BAR_COLOR, lightStatusBar = true)

        // Network checking
        val networkChecking = NetworkChecking
        connectivityStatus = networkChecking.getConnectivityStatusString(this)

        cityWeatherAdapter = CityWeatherAdapter(this)
        setUpRecyclerView()

        GlobalScope.launch {
            getCityWeatherList()
        }
    }

    //create notification channel
    private fun fetchWeatherDataAndScheduleNotification(lat: Double, lng: Double) {
        if (connectivityStatus == "Wifi enabled" || connectivityStatus == "Mobile data enabled") {
            lifecycleScope.launch {
                binding.apply {
                    viewModel.getWeatherByLatLng(lat, lng, Constants.APP_ID)
                    viewModel.latLngWeatherDetails.observe(this@MainActivity) {
                        when (it.status) {
                            DataStatus.Status.LOADING -> {
                                pBarLoading.isVisible(true, rvCityWeather)
                            }

                            DataStatus.Status.SUCCESS -> {
                                pBarLoading.isVisible(false, rvCityWeather)
                                it?.let {
                                    scheduleDailyNotification(it.data!!.main.temp)
                                }
                            }

                            DataStatus.Status.ERROR -> {
                                pBarLoading.isVisible(false, rvCityWeather)
                                Toast.makeText(
                                    this@MainActivity,
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
                this@MainActivity,
                "Internet not available!!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun scheduleDailyNotification(temp: Double) {
        val currentTime = Calendar.getInstance()
        val notificationTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 46)
            set(Calendar.SECOND, 0)
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val delay = notificationTime.timeInMillis - currentTime.timeInMillis

        val inputData = Data.Builder()
            .putDouble("TEMP", temp)
            .build()

        val oneTimeRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(this).enqueue(oneTimeRequest)

        val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyNotificationWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }


    // Call this method to check and request permissions
    private fun checkLocationPermission() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                getCurrentLocation()
            }
            else -> {
                // No location access granted.
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // Use the location object
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Do something with the location data
                    Log.e("Main", "1st$latitude$longitude")
                    fetchWeatherDataAndScheduleNotification(latitude, longitude)
                } else {
                    // Request new location data if the last known location is null
                    requestNewLocationData()
                }
            }
    }

    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult
            for (location in locationResult.locations) {
                // Update UI with location data
                val latitude = location.latitude
                val longitude = location.longitude
                // Do something with the location data
                Log.e("Main", "2nd$latitude$longitude")
                fetchWeatherDataAndScheduleNotification(latitude, longitude)
            }
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    private fun getCityWeatherList() {
        if (connectivityStatus == "Wifi enabled" || connectivityStatus == "Mobile data enabled") {
            lifecycleScope.launch {
                binding.apply {
                    viewModel.getCityWeatherList(Constants.lat, Constants.lon, Constants.cnt, Constants.APP_ID)
                    viewModel.cityWeatherList.observe(this@MainActivity) {
                        when (it.status) {
                            DataStatus.Status.LOADING -> {
                                pBarLoading.isVisible(true, rvCityWeather)
                            }

                            DataStatus.Status.SUCCESS -> {
                                pBarLoading.isVisible(false, rvCityWeather)
                                cityWeatherAdapter?.submitData(it.data?.list!!)
                            }

                            DataStatus.Status.ERROR -> {
                                pBarLoading.isVisible(false, rvCityWeather)
                                Toast.makeText(
                                    this@MainActivity,
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
                this@MainActivity,
                "Internet not available!!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setUpRecyclerView() {
        // Set up the RecyclerView with the character adapter
        binding.rvCityWeather.initRecycler(LinearLayoutManager(this), cityWeatherAdapter!!)
    }
}