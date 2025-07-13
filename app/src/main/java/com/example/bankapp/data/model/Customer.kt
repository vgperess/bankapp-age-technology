package com.example.bankapp.data.model

data class Customer(
    val customerName: String,
    val accountNumber: String,
    val branchNumber: String,
    val checkingAccountBalance: Double,
    val id: String
)
