package com.example.simplemoneymanager.domain.account

import androidx.room.Embedded
import androidx.room.Relation
import com.example.simplemoneymanager.domain.transaction.Transaction

data class AccountWithTransactions(
    @Embedded
    val account: Account,
    @Relation(parentColumn = "accountId", entityColumn = "accountId")
    val transactions: List<Transaction>
)
