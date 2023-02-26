package com.example.weatherapp.models

data class Astro (

	val sunrise : String,
	val sunset : String,
	val moonrise : String,
	val moonset : String,
	val moon_phase : String,
	val moon_illumination : Int,
	val is_moon_up : Int,
	val is_sun_up : Int
)