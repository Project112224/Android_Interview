package com.example.android_interview.model.loading

import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Loading {
    private val _state = MutableStateFlow(false)
    val state: StateFlow<Boolean> = _state

    fun show() {
        android.os.Handler(Looper.getMainLooper()).post {
            _state.value = true
        }
    }

    fun hide() {
        android.os.Handler(Looper.getMainLooper()).post {
            _state.value = false
        }
    }
}