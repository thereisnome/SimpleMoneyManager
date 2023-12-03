package com.example.simplemoneymanager.domain.budget

import androidx.room.Embedded
import androidx.room.Relation
import com.example.simplemoneymanager.domain.transaction.Transaction

data class BudgetWithTransactions(
    @Embedded
    val budget: Budget,
    @Relation(parentColumn = "id", entityColumn = "id")
    val transactions: List<Transaction>
)
