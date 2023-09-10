package com.berkaksoy.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.berkaksoy.weatherapp.data.ForecastResponse
import com.berkaksoy.weatherapp.data.WeatherResponse
import com.berkaksoy.weatherapp.data.WeatherUiModel
import com.berkaksoy.weatherapp.events.WeatherUiAction
import com.berkaksoy.weatherapp.events.WeatherUiState
import com.berkaksoy.weatherapp.usecase.WeatherUseCase
import com.berkaksoy.weatherapp.viewmodel.WeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel

    @RelaxedMockK
    private lateinit var weatherUseCase: WeatherUseCase

    @BeforeEach
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxed = true)
        initializeViewModel()
        coEvery {
            weatherUseCase.getWeatherData(any(),any())
        } returns WeatherUiModel(
            currentWeatherGifId = null,
            weatherData = WeatherResponse(main = null, weather = listOf(), wind = null, name = null),
            forecastData = ForecastResponse(list = listOf()),
            forecastMap = mutableMapOf(),
        )
    }

    @AfterEach
    fun tearDown(){
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun initializeViewModel() {
        viewModel = spyk(
            WeatherViewModel(weatherUseCase = weatherUseCase),
            recordPrivateCalls = true
        )
    }

    @Test
    fun `Verify Uninitialized state triggered`() = runTest {
        viewModel.dispatch(WeatherUiAction.WeatherDataCleared(""))

        val states = mutableListOf<WeatherUiState>()
        val job = launch {
            viewModel.stateFlow.toList(states)
        }

        advanceUntilIdle()

        assertTrue(states.any { it is WeatherUiState.Uninitialized })
        job.cancel()
    }

    @Test
    fun `Verify Loading state triggered`() = runTest {
        viewModel.dispatch(WeatherUiAction.GetCurrentLocation(""))

        val states = mutableListOf<WeatherUiState>()
        val job = launch {
            viewModel.stateFlow.toList(states)
        }

        advanceUntilIdle()

        assertTrue(states.any { it is WeatherUiState.Loading })
        job.cancel()
    }

    @Test
    fun `Verify WeatherDataLoaded state triggered`() = runTest {
        viewModel.dispatch(WeatherUiAction.WeatherDataClicked(0.0,0.0))

        val states = mutableListOf<WeatherUiState>()
        val job = launch {
            viewModel.stateFlow.toList(states)
        }

        advanceUntilIdle()

        assertTrue(states.any { it is WeatherUiState.WeatherDataLoaded })
        job.cancel()
    }
}