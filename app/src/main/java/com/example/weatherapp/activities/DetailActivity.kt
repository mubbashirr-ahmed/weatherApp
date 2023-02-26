package com.example.weatherapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.adapters.DetailAdapter
import com.example.weatherapp.databinding.ActivityDetailBinding
import com.example.weatherapp.models.Forecastday
import com.example.weatherapp.models.KeysQueries
import com.example.weatherapp.models.WeatherResponse


class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViews()
    }

    private fun setViews() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val result = KeysQueries.weatherResponse
        binding.tvTemp.text = result.current.temp_c.toString() + "°"
        binding.tvDesc.text = result.current.condition.text
        val url = result.current.condition.icon
        url.let {
            val imgUri = url.toUri().buildUpon().scheme("https").build()
            binding.ivToday.load(imgUri)
        }
        setRecycler(result.forecast.forecastday)
    }

    private fun setRecycler(forecastday: List<Forecastday>) {
        binding.rvDetails.layoutManager = LinearLayoutManager(this)
        binding.rvDetails.adapter = DetailAdapter(this, forecastday)
    }


}