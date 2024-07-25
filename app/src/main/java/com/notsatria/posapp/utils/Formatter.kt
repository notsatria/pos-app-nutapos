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

// Mendapatkan date sekarang
fun getCurrentDate(): Date {
    val calendar = Calendar.getInstance()
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val formattedDate = simpleDateFormat.format(calendar.time)

    return simpleDateFormat.parse(formattedDate)!!
}