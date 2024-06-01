package com.mhs.weatherapp.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mhs.weatherapp.databinding.CityWeatherItemViewBinding
import com.mhs.weatherapp.model.CityWeatherListResponse
import com.mhs.weatherapp.view.DetailsWeatherInfo
import javax.inject.Inject
import kotlin.math.roundToInt

class CityWeatherAdapter @Inject constructor(private val context: Context) : RecyclerView.Adapter<CityWeatherAdapter.MyViewHolder>() {

    private val data: MutableList<CityWeatherListResponse.CityWeather> = mutableListOf()

    inner class MyViewHolder(private val itemBinding: CityWeatherItemViewBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        // Bind data to the views
        @SuppressLint("SetTextI18n")
        fun bind(item: CityWeatherListResponse.CityWeather) {
            itemBinding.apply {
                tvNameCity.text = item.name
                tvDescription.text = item.weather[0].description
                val mainTemp: Double = item.main.temp
                val celsiusTemp: Double = (mainTemp - 273.15)
                tvTemp.text = "${celsiusTemp.roundToInt()}°c"

                // Set onClickListener for the entire item
                root.setOnClickListener {
                    val intent = Intent(context, DetailsWeatherInfo::class.java)
                    // Pass specific values from the clicked item as extras
                    intent.putExtra("CITY_NAME", item.name)
                    // Start the DetailsWeatherInfo activity
                    context.startActivity(intent)
                }
            }
        }
    }

    // onCreateViewHolder is called when the RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate the layout for the item view
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityWeatherItemViewBinding.inflate(inflater, parent, false)
        // Return a new instance of MyViewHolder with the inflated binding
        return MyViewHolder(binding)
    }

    // onBindViewHolder is called to bind data to a ViewHolder at a given position
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Call the bind method of the ViewHolder to bind data to views
        holder.bind(data[position])
        // Disable recycling of ViewHolder to keep its state
        holder.setIsRecyclable(false)
    }

    // getItemCount returns the number of items in the data list
    override fun getItemCount(): Int = data.size

    // submitData is a custom method to update the data in the adapter
    fun submitData(newData: List<CityWeatherListResponse.CityWeather>) {
        // Clear existing data and add the new data
        data.clear()
        data.addAll(newData)
        // Notify the adapter that the data set has changed
        notifyDataSetChanged()
    }
}