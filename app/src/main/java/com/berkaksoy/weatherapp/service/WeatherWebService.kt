package com.berkaksoy.weatherapp.service

import com.berkaksoy.weatherapp.BuildConfig
import com.berkaksoy.weatherapp.data.ForecastResponse
import com.berkaksoy.weatherapp.data.WeatherResponse
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Query

interface WeatherWebService {

    @POST("weather?")
    suspend fun getWeatherInfo(
        @Query("lat") lat: Double?,
        @Query("lon") long: Double?,
        @Query("appid") appid: String? = BuildConfig.APP_ID,
        @Query("units") units: String? = "metric",
    ): Response<WeatherResponse>

    @POST("forecast?")
    suspend fun getForecast(
        @Query("lat") lat: Double?,
        @Query("lon") long: Double?,
        @Query("appid") appid: String? = BuildConfig.APP_ID,
        @Query("units") units: String? = "metric",
    ): Response<ForecastResponse>
}