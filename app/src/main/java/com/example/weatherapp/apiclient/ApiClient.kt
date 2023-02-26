package com.example.weatherapp.apiclient

import com.example.weatherapp.services.WeatherServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val URL = "https://api.weatherapi.com/v1/";
    private  val client:OkHttpClient.Builder = OkHttpClient.Builder()
        .callTimeout(15, TimeUnit.SECONDS)
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client.build()).build()

    fun buildService(): WeatherServices {
        return retrofit.create(WeatherServices::class.java)
    }
}
