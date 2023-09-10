package com.berkaksoy.weatherapp.data

import com.berkaksoy.weatherapp.R

enum class GifIdEnum(val code: String, val id: Int) {
    THUNDERSTORM("Thunderstorm", R.drawable.ic_thunderstrom),
    DRIZZLE("Drizzle", R.drawable.ic_drizzle),
    RAIN("Rain", R.drawable.ic_rainy),
    CLEAR("Clear", R.drawable.ic_sunny),
    CLOUD("Clouds", R.drawable.ic_cloudy),
    SNOW("Snow", R.drawable.ic_snowy),
}