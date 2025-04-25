package com.example.android_interview.viewModel.tour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_interview.model.response.ScenicSpotResponse
import com.example.android_interview.network.manager.ApiManager
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class TourViewModel : ViewModel() {

    private val tourRepository = ApiManager.tourRepository

    private var _tourInfo = MutableSharedFlow<List<ScenicSpotResponse>?>(1)
    val tourInfo: SharedFlow<List<ScenicSpotResponse>?>
        get () = _tourInfo

    fun fetchTourInfo() {
        viewModelScope.launch {
            tourRepository.getTourInfo()
                .suspendOnSuccess {
                    _tourInfo.emit(this.data)
                }
                .suspendOnFailure {
                    Timber.i("error: ${this.message()}")
                }
        }
    }

}