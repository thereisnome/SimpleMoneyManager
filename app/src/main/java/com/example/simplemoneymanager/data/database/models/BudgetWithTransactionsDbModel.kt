package com.example.simplemoneymanager.data.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithTransactionsDbModel(
    @Embedded
    val budget: BudgetDbModel,
    @Relation(parentColumn = "id", entityColumn = "id")
    val transactions: List<TransactionDbModel>
)
