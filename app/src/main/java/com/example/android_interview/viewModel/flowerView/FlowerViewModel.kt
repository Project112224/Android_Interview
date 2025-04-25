package com.example.android_interview.viewModel.flowerView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_interview.model.response.AttractionResponse
import com.example.android_interview.network.manager.ApiManager
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

class FlowerViewModel : ViewModel() {

    private val tourRepository = ApiManager.tourRepository

    private var originData: MutableList<AttractionResponse>  = arrayListOf()

    private var page = 0

    private var _showComment = MutableStateFlow(false)
    val showComment: StateFlow<Boolean>
        get() = _showComment

    private var _attractionsData = MutableSharedFlow<List<AttractionResponse>>(1)
    val attractionsData: SharedFlow<List<AttractionResponse>>
        get() = _attractionsData

    fun fetchAttractions() {
        viewModelScope.launch {
            tourRepository.getAttractions(page)
                .suspendOnSuccess {
                    originData.addAll(data.toMutableList())
                    page = abs(originData.size / 20)
                    _attractionsData.emit(originData)
                    Timber.i("data: $data")
                }
                .suspendOnFailure {
                    Timber.e(this.message())
                }
        }
    }

    fun refreshAttractions() {
        viewModelScope.launch {
            originData.clear()
            tourRepository.getAttractions(0)
                .suspendOnSuccess {
                    originData.addAll(data.toMutableList())
                    page = abs(originData.size / 20)
                    _attractionsData.emit(originData)
                }
                .suspendOnFailure {
                    Timber.e(this.message())
                }
        }
    }

    fun setCommentVisit(state: Boolean) {
        viewModelScope.launch {
            Timber.i("comment state: $state")
            _showComment.emit(state)
        }
    }

    fun filter(word: String) {
        if (word.isEmpty()) {
            viewModelScope.launch {
                Timber.i("word not empty.")
                _attractionsData.emit(originData)
            }
            return
        }
        val result = originData.filter {
            it.flowerName.contains(word, ignoreCase = true) || it.plate.contains(word, ignoreCase = true)
        }
        viewModelScope.launch {
            _attractionsData.emit(result.distinctBy { it.plate })
        }
    }
}