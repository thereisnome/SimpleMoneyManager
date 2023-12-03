package com.example.simplemoneymanager.domain.budget.usecases

import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import io.reactivex.rxjava3.core.Completable

class AddBudgetUseCase(private val budgetRepository: BudgetRepository) {

    operator fun invoke(budget: Budget): Completable {
        return budgetRepository.addBudget(budget)
    }
}