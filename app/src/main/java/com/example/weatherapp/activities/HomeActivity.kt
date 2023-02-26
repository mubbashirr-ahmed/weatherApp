package com.example.weatherapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.adapters.HoursAdapter
import com.example.weatherapp.apiclient.ApiClient
import com.example.weatherapp.databinding.ActivityHomeBinding
import com.example.weatherapp.models.Hour
import com.example.weatherapp.models.KeysQueries
import com.example.weatherapp.models.WeatherResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var cityName = "Rawalpindi"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inIT()
    }

    private fun inIT() {
        setViews()
        if (checkInternet()) {
            callAPI()
            clickListeners()
            return
        }
        Snackbar.make(
            findViewById(android.R.id.content),
            "No Internet Connection",
            Snackbar.LENGTH_LONG
        ).show()

    }

    private fun checkInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        )
    }

    private fun setViews() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun clickListeners() {

        binding.nextdays.setOnClickListener {
            startActivity(Intent(this@HomeActivity, DetailActivity::class.java))
        }
        binding.ivMic.setOnClickListener {
            getResult.launch(Intent(this@HomeActivity, StartActivity::class.java))
        }

    }

    private fun callAPI() {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["key"] = BuildConfig.MY_API_KEY
        hashMap["q"] = cityName
        hashMap["days"] = "7"
        hashMap["aqi"] = "no"
        hashMap["alert"] = "no"
        CoroutineScope(Dispatchers.IO).launch {
            val weatherServices = ApiClient.buildService()
            val reqCall = weatherServices.getWeather(hashMap)
            val result = reqCall.body()
            withContext(Dispatchers.Main) {
                if (result == null) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Invalid City Name",
                        Snackbar.LENGTH_LONG
                    ).show()
                    return@withContext
                }
                KeysQueries.weatherResponse = result
                upDateUI(result)
                setHourAdapter(result.forecast.forecastday[0].hour)
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun upDateUI(result: WeatherResponse) {

        binding.postLayout.tvTemp.text = result.current.temp_c.toString() + "°"
        binding.postLayout.tvDesc.text = result.current.condition.text
        binding.postLayout.tvDateTime.text = LocalDate.now().dayOfWeek.name

        binding.tvPerception.text = result.current.precip_mm.toString() + "mm"
        binding.tvWind.text = result.current.wind_kph.toString() + "km/h"
        binding.tvPressure.text = result.current.pressure_in.toString() + "in"
        binding.tvVisibility.text = result.current.vis_km.toString() + "km"
        binding.cityName.text = result.location.name

        binding.time.text =
            SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH).format(Date()).drop(10)


        result.current.condition.icon.let {
            val imgUri = result.current.condition.icon.toUri().buildUpon().scheme("https").build()
            binding.postLayout.ivImages.load(imgUri)
        }

    }

    private fun setHourAdapter(hour: List<Hour>) {
        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recycler.adapter = HoursAdapter(this, hour)
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getStringExtra("cityName")
                cityName = result.toString()
                callAPI()
            }
        }
}
