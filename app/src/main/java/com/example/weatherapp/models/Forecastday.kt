package com.example.weatherapp.models

data class Forecastday (

    val date : String,
    val date_epoch : Int,
    val day : Day,
    val astro : Astro,
    val hour : List<Hour>
)