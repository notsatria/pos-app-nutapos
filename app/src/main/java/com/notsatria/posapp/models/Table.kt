package com.notsatria.posapp.models

data class Header(val date: String)
data class Item(
    val time: String,
    val to: String,
    val from: String,
    val description: String,
    val amount: String
)

data class Footer(val total: String)
