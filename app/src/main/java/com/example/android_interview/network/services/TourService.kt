package com.example.android_interview.network.services

import com.example.android_interview.model.response.AttractionResponse
import com.example.android_interview.model.response.ScenicSpotResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TourService {

    @GET("f116d1db-56f7-4984-bad8-c82e383765c0")
    suspend fun getAttractions(
        @Query("offset") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): ApiResponse<List<AttractionResponse>>

    @GET("38476e5e-9288-4b83-bb33-384b1b36c570")
    suspend fun getTourInfo(): ApiResponse<List<ScenicSpotResponse>>
}