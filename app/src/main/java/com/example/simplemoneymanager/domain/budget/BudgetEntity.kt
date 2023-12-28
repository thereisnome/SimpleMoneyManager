package com.example.simplemoneymanager.domain.budget

import com.example.simplemoneymanager.domain.category.CategoryEntity

data class BudgetEntity(
    var maxValue: Double,
    val budgetId: Long = 0,
    val category: CategoryEntity
)