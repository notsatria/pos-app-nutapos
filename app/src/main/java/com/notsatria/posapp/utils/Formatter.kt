package com.notsatria.posapp.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

// Format rupiah currency
fun formatRupiah(value: Int): String {
    val localeID = Locale("id", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    numberFormat.maximumFractionDigits = 0
    return numberFormat.format(value)
}

// Mendapatkan waktu sekarang dengan format HH:mm:ss
fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return currentTime.format(formatter)
}

// Convert timestamp ke String
fun convertTimestampToString(timestamp: Long, format: String = "dd MMMM yyyy"): String {
    val sdf = SimpleDateFormat(format, Locale.US)
    val date = Date(timestamp)
    return sdf.format(date)
}

fun getStartOfDayInMillis(date: Long): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = date
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun getEndOfDayInMillis(date: Long): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = date
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return calendar.timeInMillis
}