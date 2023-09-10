package com.berkaksoy.weatherapp.data

import com.google.gson.annotations.SerializedName

data class ForecastResponse (
    @SerializedName("list")
    var list: List<ForecastList>? = null
)

data class ForecastList (
    @SerializedName("main")
    var main: ForecastMain? = null,
    @SerializedName("dt_txt")
    var dateText: String? = null
)

data class ForecastMain (
    @SerializedName("temp_min")
    var tempMin: Double? = 0.0,
    @SerializedName("temp_max")
    var tempMax: Double? = 0.0
)