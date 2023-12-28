package com.example.simplemoneymanager.domain.account

import com.example.simplemoneymanager.domain.transaction.TransactionEntity

data class AccountWithTransactionsEntity(
    val account: AccountEntity,
    val transactions: List<TransactionEntity>
)
