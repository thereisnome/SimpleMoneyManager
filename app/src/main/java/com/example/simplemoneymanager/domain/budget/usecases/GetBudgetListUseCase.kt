package com.example.simplemoneymanager.domain.budget.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.repository.BudgetRepository

class GetBudgetListUseCase(private val budgetRepository: BudgetRepository) {

    operator fun invoke(): LiveData<List<Budget>> {
        return budgetRepository.getBudgetList()
    }
}