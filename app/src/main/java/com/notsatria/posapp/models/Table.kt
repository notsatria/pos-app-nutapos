package com.notsatria.posapp.models

data class Header(val date: String, var amount: Int?)
data class Item(
    val time: String,
    val to: String,
    val from: String,
    val description: String,
    val amount: Int
)

data class Footer(val total: String)
