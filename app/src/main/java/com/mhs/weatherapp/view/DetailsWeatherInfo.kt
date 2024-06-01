package com.mhs.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mhs.weatherapp.R
import com.mhs.weatherapp.adapter.CityWeatherAdapter
import com.mhs.weatherapp.databinding.ActivityMainBinding
import com.mhs.weatherapp.model.CityWeatherListResponse
import com.mhs.weatherapp.viewModel.MainViewModel

class DetailsWeatherInfo : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var connectivityStatus: String? = null
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

        binding.toolbarLayout.ivBack.setOnClickListener {
            finish()
        }

        // Retrieve the value for "CITY_NAME" extra from the Intent
        val cityName = intent.getStringExtra("CITY_NAME")
        Log.e("Details", "" + cityName)
    }
}