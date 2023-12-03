package com.example.simplemoneymanager.domain.budget.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.repository.BudgetRepository

class GetBudgetByIdUseCase(private val budgetRepository: BudgetRepository) {

    operator fun invoke(budgetId: Long): LiveData<Budget> {
        return budgetRepository.getBudgetById(budgetId)
    }
}