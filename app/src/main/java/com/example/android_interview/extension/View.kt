package com.example.android_interview.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.closeSoftKeyBoard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
}
