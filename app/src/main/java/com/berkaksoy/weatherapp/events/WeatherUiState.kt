package com.berkaksoy.weatherapp.events

import com.berkaksoy.weatherapp.data.WeatherUiModel
import com.berkaksoy.weatherapp.viewmodel.BaseState

sealed class WeatherUiState : BaseState {

    data class Uninitialized(
        val data: String? = null
    ) : WeatherUiState()

    data class Loading(
        val data: String? = null
    ) : WeatherUiState()

    data class WeatherDataLoaded(
        val uiModel: WeatherUiModel?
    ) : WeatherUiState()
}