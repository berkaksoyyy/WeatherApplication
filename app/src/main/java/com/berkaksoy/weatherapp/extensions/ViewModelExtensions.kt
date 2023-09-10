package com.berkaksoy.weatherapp.extensions

import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import com.berkaksoy.weatherapp.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.onEach


fun <V : BaseViewModel<*, E>, E> ComponentActivity.withEvent(
    viewModel: V,
    onEventPushed: suspend (E) -> Unit
) = collectEvent(viewModel, this, onEventPushed)

private fun <V : BaseViewModel<*, E>, E> collectEvent(
    viewModel: V,
    lifecycleOwner: LifecycleOwner,
    onEventPushed: suspend (E) -> Unit
) = viewModel.eventFlow
    .onEach(onEventPushed)
    .collectIn(lifecycleOwner)
