package com.berkaksoy.weatherapp.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("main")
    var main: CurrentTemp? = null,
    @SerializedName("weather")
    var weather: List<CurrentWeather?>? = null,
    @SerializedName("wind")
    var wind: CurrentWind? = null,
    @SerializedName("name")
    var name: String? = null,
)

data class CurrentTemp(
    @SerializedName("temp")
    var temp: Double? = null,
    @SerializedName("temp_min")
    var tempMin: Double? = null,
    @SerializedName("temp_max")
    var tempMax: Double? = null,
    @SerializedName("feels_like")
    var feelsLike: Double? = null,
    @SerializedName("humidity")
    var humidity: Double? = null,
)

data class CurrentWeather(
    @SerializedName("main")
    var main: String? = null,
    @SerializedName("description")
    var description: String? = null,
)

data class CurrentWind(
    @SerializedName("speed")
    var speed: Double? = null
)