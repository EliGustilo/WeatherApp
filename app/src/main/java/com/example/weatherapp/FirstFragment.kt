package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.FragmentFirstBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import java.net.URL

data class MeteoResponse (
    @SerializedName("timezone")
    var timezone: String? = null,
    @SerializedName("elevation")
    var elevation: Int? = null,
    @SerializedName("current_weather")
    var current_weather: Weather? = null,
)

data class Weather (
    @SerializedName("temperature")
    var temperature: Double? = null,
    @SerializedName("windspeed")
    var windspeed: Double? = null,
    @SerializedName("winddirection")
    var wind_direction: Int? = null,
    @SerializedName("weathercode")
    var weather_code: Int? = null,
    @SerializedName("is_day")
    var is_day: Int? = null
)

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        binding.weatherButton.setOnClickListener {
            if (isOnline(requireContext())){
                getWeather()
            } else  {
                Toast.makeText(requireContext(), "You aren't connected to the web!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getWeather(){
        var latitude = 0.0
        var longitude = 0.0

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // Permission has already been granted
            fusedLocationClient.lastLocation.addOnSuccessListener { location->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d("location", "Latitude: $latitude")
                    Log.d("location", "Longitude: $longitude")
                    lifecycleScope.launch(Dispatchers.IO) {
                        val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current_weather=true"
                        val apiResponse = URL(url).readText()
                        Log.d("getWeather", apiResponse)
                        val weatherData = Gson().fromJson(apiResponse, MeteoResponse::class.java)
                        displayWeather(weatherData)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayWeather(weatherData: MeteoResponse){
        lifecycleScope.launch(Dispatchers.Main) {

            when (weatherData.current_weather!!.weather_code) {
                0 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_day_sunny)
                    binding.descriptionTextView.text = "Clear Sky"
                }
                1 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_day_sunny)
                    binding.descriptionTextView.text = "Mainly Clear"
                }
                2 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_day_sunny_overcast)
                    binding.descriptionTextView.text = "Partly Cloudy"
                }
                3 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_day_sunny_overcast)
                    binding.descriptionTextView.text = "Overcast"
                }
                45 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_fog)
                    binding.descriptionTextView.text = "Fog"
                }
                48 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_fog)
                    binding.descriptionTextView.text = "Rime Fog"
                }
                51 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_sprinkle)
                    binding.descriptionTextView.text = "Light Drizzle"
                }
                53 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_sprinkle)
                    binding.descriptionTextView.text = "Moderate Drizzle"
                }
                55 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_sprinkle)
                    binding.descriptionTextView.text = "Intense Drizzle"
                }
                56 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_sleet)
                    binding.descriptionTextView.text = "Light Freezing Drizzle"
                }
                57 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_sleet)
                    binding.descriptionTextView.text = "Dense Freezing Drizzle"
                }
                61 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_rain)
                    binding.descriptionTextView.text = "Slight Rain"
                }
                63 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_rain_mix)
                    binding.descriptionTextView.text = "Moderate Rain"
                }
                65 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_rain_wind)
                    binding.descriptionTextView.text = "Heavy Rain"
                }
                66 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_sleet)
                    binding.descriptionTextView.text = "Light Freezing Rain"
                }
                67 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_sleet)
                    binding.descriptionTextView.text = "Heavy Freezing Rain"
                }
                71 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_snow)
                    binding.descriptionTextView.text = "Slight Snow"
                }
                73 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_snow)
                    binding.descriptionTextView.text = "Moderate Snow"
                }
                75 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_snow_wind)
                    binding.descriptionTextView.text = "Heavy Snow"
                }
                77 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_snow)
                    binding.descriptionTextView.text = "Snow Grains"
                }
                80 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_rain)
                    binding.descriptionTextView.text = "Slight Rain showers"
                }
                81 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_rain_mix)
                    binding.descriptionTextView.text = "Moderate Rain Showers"
                }
                82 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_rain_wind)
                    binding.descriptionTextView.text = "Violent Rain Showers"
                }
                85 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_snow)
                    binding.descriptionTextView.text = "Slight Snow Showers"
                }
                86 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_snow_wind)
                    binding.descriptionTextView.text = "Heavy Snow Showers"
                }
                95 -> {
                    binding.weatherImageView.setImageResource(R.drawable.ic_thunderstorm)
                    binding.descriptionTextView.text = "Thunderstorm"
                }
            }

            binding.temperatureTextView.text = "Temperature: ${weatherData.current_weather!!.temperature.toString()}"
            binding.windspeedTextView.text = "Wind Speed: ${weatherData.current_weather!!.windspeed.toString()}"

            binding.weatherImageView.visibility = View.VISIBLE
            binding.descriptionTextView.visibility = View.VISIBLE
            binding.temperatureTextView.visibility = View.VISIBLE
            binding.windspeedTextView.visibility = View.VISIBLE
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}