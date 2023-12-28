package com.example.simplemoneymanager.domain.category

import com.example.simplemoneymanager.domain.transaction.TransactionEntity

data class CategoryWithTransactionsEntity(
    val category: CategoryEntity,
    val transactions: List<TransactionEntity>
)
