package com.example.android_interview.network.manager

import com.example.android_interview.BuildConfig
import com.example.android_interview.Global
import com.example.android_interview.extension.unicode
import com.example.android_interview.util.GlobalEventBus
import com.moczul.ok2curl.CurlInterceptor
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import com.moczul.ok2curl.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object AppLogger :
    HttpLoggingInterceptor.Logger by (HttpLoggingInterceptor.Logger { message: String ->
        val result = message.unicode()
        val str: String = try {
            JSONObject(result).toString(4)
        } catch (e: JSONException) {
            result
        }
        Platform.get().log(str)
    })

object RetrofitManager {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.SECONDS)
            .addInterceptor(NetworkExceptionInterceptor { throwable ->
                when (throwable) {
                    is SocketTimeoutException -> {
                        Timber.e("Timeout")
                        GlobalEventBus.post(GlobalEventBus.GlobalEvent.Timeout)
                    }
                    // 無網路
                    is UnknownHostException, is ConnectException -> {
                        Timber.e("網路未連線")
                        GlobalEventBus.post(GlobalEventBus.GlobalEvent.NoNetwork)
                    }
                    else -> {
                        Timber.e("其他網路錯誤: ${throwable.message}")
                        GlobalEventBus.post(GlobalEventBus.GlobalEvent.HttpError(4001, throwable.message))
                    }
                }
            })

        if (BuildConfig.DEBUG) {
            val curlInterceptor = CurlInterceptor(object : Logger {
                override fun log(message: String) {
                    Timber.v("Ok2Curl: $message")
                }
            })
            builder.addInterceptor(curlInterceptor)
            val loggingInterceptor = HttpLoggingInterceptor(AppLogger)
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }
        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Global.API_END_POINT)
            .client(okHttpClient)
            .build()
    }

    fun <S> create(serviceClass: Class<S>): S = retrofit.create(serviceClass)
}

class NetworkExceptionInterceptor(
    private val onError: (Throwable) -> Unit
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: IOException) {
            onError(e)
            throw e
        }
    }
}