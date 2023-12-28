package com.example.simplemoneymanager.domain.budget.usecases

import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EditBudgetUseCase @Inject constructor(private val budgetRepository: BudgetRepository) {

    operator fun invoke(budget: BudgetEntity): Completable {
        return budgetRepository.editBudgetById(budget)
    }
}