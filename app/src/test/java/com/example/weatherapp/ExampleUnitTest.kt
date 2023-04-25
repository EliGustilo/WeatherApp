package com.example.weatherapp

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.junit.Test

import org.junit.Assert.*
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

data class MeteoResponse (
    @SerializedName("latitude")
    var latitude: Double? = null,
    @SerializedName("longitude")
    var longitude: Double? = null,
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

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class APIUnitTest {
    @Test
    fun hitApi(){
        val latitude = 0.0
        val longitude = 0.0
        val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current_weather=true"
        val apiResponse = URL(url).readText()
        val weatherData = Gson().fromJson(apiResponse, MeteoResponse::class.java)
        assert(apiResponse.isNotEmpty())
        assert(weatherData.latitude == 0.0)
        assert(weatherData.longitude == 0.0)
    }
}