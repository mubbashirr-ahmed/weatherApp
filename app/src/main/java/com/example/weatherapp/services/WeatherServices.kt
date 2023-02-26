package com.example.weatherapp.services
import com.example.weatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherServices {


    @GET("forecast.json")
    suspend fun getWeather(@QueryMap key:HashMap<String, String>): Response<WeatherResponse>
}