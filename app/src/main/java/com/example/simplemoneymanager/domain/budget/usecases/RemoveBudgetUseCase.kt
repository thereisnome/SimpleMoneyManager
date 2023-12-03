package com.example.simplemoneymanager.domain.budget.usecases

import com.example.simplemoneymanager.domain.repository.BudgetRepository
import io.reactivex.rxjava3.core.Completable

class RemoveBudgetUseCase(private val budgetRepository: BudgetRepository) {

    operator fun invoke(budgetId: Long): Completable {
        return budgetRepository.removeBudget(budgetId)
    }
}