package com.berkaksoy.weatherapp.events

import com.berkaksoy.weatherapp.viewmodel.BaseEvent

sealed class WeatherEvent : BaseEvent {

    data class GetCurrentLocation(
        val data: String
    ) : WeatherEvent()
}