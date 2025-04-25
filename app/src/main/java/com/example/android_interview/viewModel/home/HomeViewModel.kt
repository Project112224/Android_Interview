package com.example.android_interview.viewModel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private var _isEnglish = MutableStateFlow(false)
    val isEnglish: StateFlow<Boolean> get() = _isEnglish

    fun triggerLanguage() : Boolean {
        val isEnglish = _isEnglish.value
        val trigger = !isEnglish
        setLanguage(trigger)
        return trigger
    }

    fun setLanguage(state: Boolean) {
        viewModelScope.launch {
            _isEnglish.emit(state)
            Timber.i("language is english: $state")
        }
    }

}