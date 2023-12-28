package com.example.simplemoneymanager.data.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithTransactionsDbModel(
    @Embedded
    val account: AccountDbModel,
    @Relation(parentColumn = "accountId", entityColumn = "accountId")
    val transactions: List<TransactionDbModel>
)
