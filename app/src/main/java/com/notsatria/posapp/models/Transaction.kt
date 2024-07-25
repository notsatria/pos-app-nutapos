package com.notsatria.posapp.models

data class Transaction(
    val time: String,
    val to: String,
    val from: String,
    val description: String,
    val amount: String,
    val date: String,
)
