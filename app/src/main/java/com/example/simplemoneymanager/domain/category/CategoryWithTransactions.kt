package com.example.simplemoneymanager.domain.category

import androidx.room.Embedded
import androidx.room.Relation
import com.example.simplemoneymanager.domain.transaction.Transaction

data class CategoryWithTransactions(
    @Embedded
    val category: Category,
    @Relation(parentColumn = "id", entityColumn = "id")
    val transactions: List<Transaction>
)
