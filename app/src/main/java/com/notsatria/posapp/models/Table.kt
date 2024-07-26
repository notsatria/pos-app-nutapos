package com.notsatria.posapp.models

data class Header(val date: String, var amount: Int?)
data class Item(
    var id: Int = 0,
    val time: String,
    val to: String,
    val from: String,
    val description: String,
    val amount: Int,
    val type: String,
    val imageUri: String?,
)

data class Footer(val total: String)
