package com.example.simplemoneymanager.data.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithTransactionsDbModel(
    @Embedded
    val category: CategoryDbModel,
    @Relation(parentColumn = "id", entityColumn = "id")
    val transactions: List<TransactionDbModel>
)
