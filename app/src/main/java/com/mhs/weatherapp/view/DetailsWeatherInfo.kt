package com.mhs.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mhs.weatherapp.R
import com.mhs.weatherapp.databinding.ActivityDetailsWeatherInfoBinding
import com.mhs.weatherapp.utils.DataStatus
import com.mhs.weatherapp.utils.NetworkChecking
import com.mhs.weatherapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import isVisible
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsWeatherInfo : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsWeatherInfoBinding
    private val viewModel: MainViewModel by viewModels()
    private var connectivityStatus: String? = null

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
}