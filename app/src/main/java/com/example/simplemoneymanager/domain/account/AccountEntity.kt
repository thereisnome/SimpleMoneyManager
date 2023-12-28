package com.example.simplemoneymanager.domain.account

data class AccountEntity(
    val accountName: String,
    var balance: Double,
    val accountId: Long = 0,
    val accountColor: String
)