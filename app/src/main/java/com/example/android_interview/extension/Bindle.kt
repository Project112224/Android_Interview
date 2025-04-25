package com.example.android_interview.extension

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    BundleCompat.getParcelable(this, key, T::class.java)

inline fun <reified T : Parcelable> Bundle.parcelableList(key: String): List<T>? =
    BundleCompat.getParcelableArrayList(this, key, T::class.java)