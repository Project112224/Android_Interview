package com.example.android_interview.util

import android.content.Intent
import android.net.Uri
import com.example.android_interview.Global
import com.example.android_interview.util.types.PackageType

object ActionUtil {

    fun onGoogleMap(uri: Uri): Intent {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(PackageType.GOOGLE_MAP.path)
        return intent
    }
}