package com.berkaksoy.weatherapp.events


sealed interface WeatherUiAction {

    data class WeatherDataClicked(
        val lat: Double,
        val long: Double,
    ) : WeatherUiAction

    data class WeatherDataCleared(
        val extra: String
    ) : WeatherUiAction

    data class GetCurrentLocation(
        val extra: String
    ) : WeatherUiAction
}