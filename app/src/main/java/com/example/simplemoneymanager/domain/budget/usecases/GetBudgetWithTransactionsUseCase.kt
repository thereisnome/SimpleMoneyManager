package com.example.simplemoneymanager.domain.budget.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactions
import com.example.simplemoneymanager.domain.repository.BudgetRepository

class GetBudgetWithTransactionsUseCase(private val budgetRepository: BudgetRepository) {

    operator fun invoke(): LiveData<List<BudgetWithTransactions>> {
        return budgetRepository.getBudgetWithTransactions()
    }
}