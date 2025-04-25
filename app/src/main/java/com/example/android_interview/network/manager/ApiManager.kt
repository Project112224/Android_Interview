package com.example.android_interview.network.manager

import com.example.android_interview.network.repositories.TourRepository
import com.example.android_interview.network.services.TourService

object ApiManager {

    val tourRepository: TourRepository by lazy {
        TourRepository(RetrofitManager.create(TourService::class.java))
    }

}