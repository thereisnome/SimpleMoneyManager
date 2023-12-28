package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactionsEntity
import io.reactivex.rxjava3.core.Completable

interface BudgetRepository {

    fun addBudget(budget: BudgetEntity): Completable

    fun getBudgetList(): LiveData<List<BudgetEntity>>

    fun getBudgetById(budgetId: Long): LiveData<BudgetEntity>

    fun removeBudget(budgetId: Long): Completable

    fun getBudgetWithTransactions(): LiveData<List<BudgetWithTransactionsEntity>>

    fun editBudgetById(budget: BudgetEntity): Completable
}