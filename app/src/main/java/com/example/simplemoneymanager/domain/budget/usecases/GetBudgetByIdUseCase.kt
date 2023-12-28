package com.example.simplemoneymanager.domain.budget.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetByIdUseCase @Inject constructor(private val budgetRepository: BudgetRepository) {

    operator fun invoke(budgetId: Long): LiveData<BudgetEntity> {
        return budgetRepository.getBudgetById(budgetId)
    }
}