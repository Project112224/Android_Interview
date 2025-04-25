package com.example.android_interview

import android.content.res.Resources
import android.graphics.Rect
import android.net.Uri
import android.view.Window

object Global {

    const val API_END_POINT = "https://datacenter.taichung.gov.tw/swagger/OpenData/"

    const val HEADER_HEIGHT = 200

    fun getMapByUrl(url : String) : Uri = Uri.parse("http://maps.google.com/maps?q=$url")

    fun getMapByGEO(label : String, latitude : String, longitude : String) : Uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
}