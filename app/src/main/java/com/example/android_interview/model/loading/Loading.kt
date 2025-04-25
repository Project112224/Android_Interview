package com.example.android_interview.model.loading

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Loading {
    private val _state = MutableStateFlow(false)
    val state: StateFlow<Boolean> = _state

    fun show() {
        _state.value = true
    }

    fun hide() {
        _state.value = false
    }
}