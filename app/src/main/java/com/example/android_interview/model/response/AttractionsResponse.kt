package com.example.android_interview.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AttractionResponse(
    @Json(name = "縣市")
    val city: String,
    @Json(name = "縣市別代碼")
    val cityCode: String,
    @Json(name = "行政區")
    val district: String,
    @Json(name = "行政區代碼")
    val districtCode: String,
    @Json(name = "地點")
    val plate: String,
    @Json(name = "地址")
    val address: String,
    @Json(name = "花種")
    val flowerName: String,
    @Json(name = "觀賞時期")
    val season: String,
)
