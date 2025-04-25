package com.example.android_interview.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlobalEventBus {
    private val _events = MutableStateFlow<GlobalEvent?>(null)
    val events: StateFlow<GlobalEvent?> = _events

    fun post(event: GlobalEvent) {
        _events.value = event
    }

    sealed class GlobalEvent {
        object Timeout : GlobalEvent()
        object NoNetwork : GlobalEvent()
        data class HttpError(val code: Int, val message: String?) : GlobalEvent()
    }
}