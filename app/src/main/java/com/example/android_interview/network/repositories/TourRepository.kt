package com.example.android_interview.network.repositories

import com.example.android_interview.network.services.TourService

class TourRepository(private val service: TourService) {

    suspend fun getAttractions(page: Int) = service.getAttractions((20 * page) + 1)

    suspend fun getTourInfo() = service.getTourInfo()

}