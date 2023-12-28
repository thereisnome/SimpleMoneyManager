package com.example.simplemoneymanager.domain.budget

import com.example.simplemoneymanager.domain.transaction.TransactionEntity

data class BudgetWithTransactionsEntity(
    val budget: BudgetEntity,
    val transactions: List<TransactionEntity>
)
