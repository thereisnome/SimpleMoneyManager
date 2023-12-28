package com.example.simplemoneymanager.domain.budget.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetListUseCase @Inject constructor(private val budgetRepository: BudgetRepository) {

    operator fun invoke(): LiveData<List<BudgetEntity>> {
        return budgetRepository.getBudgetList()
    }
}