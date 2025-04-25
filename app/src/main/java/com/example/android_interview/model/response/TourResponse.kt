package com.example.android_interview.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Parcelize
@JsonClass(generateAdapter = true)
data class ScenicSpotResponse(
    @Json(name = "景點中文名稱")
    val nameZh: String,

    @Json(name = "景點英文名稱")
    val nameEn: String,

    @Json(name = "景點特色簡述(中文)")
    val descriptionZh: String,

    @Json(name = "景點特色簡述(英文)")
    val descriptionEn: String,

    @Json(name = "海域活動圖式編號")
    val oceanActivityIcons: String,

    @Json(name = "設施圖示編號")
    val facilityIcons: String,

    @Json(name = "經度")
    val longitude: String,

    @Json(name = "緯度")
    val latitude: String,

    @Json(name = "景點服務電話")
    val phone: String,

    @Json(name = "景點中文地址")
    val addressZh: String,

    @Json(name = "景點英文地址")
    val addressEn: String,

    @Json(name = "開放時間")
    val openTime: String,

    @Json(name = "開放時間備註")
    val openTimeNote: String,

    @Json(name = "開放時間英文備註")
    val openTimeNoteEn: String,

    @Json(name = "照片連結網址1")
    val photoUrl1: String,

    @Json(name = "照片中文說明1")
    val photoDescriptionZh1: String,

    @Json(name = "網址")
    val websiteUrl: String,

    @Json(name = "(選填)地圖服務連結")
    val mapUrl: String
) : Parcelable