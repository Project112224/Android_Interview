package com.example.android_interview.network.manager

import com.example.android_interview.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

val ApiInterceptor = Interceptor { chain ->
    val request: Request = chain.request().newBuilder()
        .addHeader("Accept", "application/json")
        .build()

    val response = chain.proceed(request)
    if (BuildConfig.DEBUG && !response.isSuccessful)
        Timber.e("Response Code: %s", response.code)

    response
}