package com.example.simplemoneymanager.domain.budget.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactionsEntity
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetWithTransactionsUseCase @Inject constructor(private val budgetRepository: BudgetRepository) {

    operator fun invoke(): LiveData<List<BudgetWithTransactionsEntity>> {
        return budgetRepository.getBudgetWithTransactions()
    }
}