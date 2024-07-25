package com.notsatria.posapp.utils

import java.text.NumberFormat
import java.util.Locale

// Format rupiah currency
fun formatRupiah(value: Int): String {
    val localeID = Locale("id", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    numberFormat.maximumFractionDigits = 0
    return numberFormat.format(value)
}