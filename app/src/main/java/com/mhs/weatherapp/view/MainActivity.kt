package com.mhs.weatherapp.view

import com.mhs.weatherapp.utils.DataStatus
import com.mhs.weatherapp.utils.NetworkChecking
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhs.weatherapp.R
import com.mhs.weatherapp.adapter.CityWeatherAdapter
import com.mhs.weatherapp.databinding.ActivityMainBinding
import com.mhs.weatherapp.utils.StatusBarUtils
import com.mhs.weatherapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import initRecycler
import isVisible
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var cityWeatherAdapter: CityWeatherAdapter? = null
    private var connectivityStatus: String? = null

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