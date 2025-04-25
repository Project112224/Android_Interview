package com.example.android_interview.extension

import java.time.LocalDateTime
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.unicode(): String{
    var resultString = this
    val pattern: Pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))")
    val matcher: Matcher = pattern.matcher(this)
    var ch: Char
    while (matcher.find()) {
        matcher.group(2)?.let {
            ch = it.toInt(16).toChar()
            matcher.group(1)?.let {
                resultString = resultString.replace(it, ch.toString() + "")
            }
        }
    }
    return resultString
}

fun String.isInBloomByRange() : Boolean {
    val dateList = this
        .replace("月", "")
        .replace(" ", "")
        .split("-")
        .map { Integer.parseInt(it) }

    val startDate = dateList.firstOrNull() ?: 0
    val endDate = dateList.lastOrNull() ?: 0
    val current = LocalDateTime.now().monthValue
    return (current in startDate..12) && (current in 1..endDate)
}