package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactions
import io.reactivex.rxjava3.core.Completable

interface BudgetRepository {

    fun addBudget(budget: Budget): Completable

    fun getBudgetList(): LiveData<List<Budget>>

    fun getBudgetById(budgetId: Long): LiveData<Budget>

    fun removeBudget(budgetId: Long): Completable

    fun getBudgetWithTransactions(): LiveData<List<BudgetWithTransactions>>

    fun editBudgetById(budget: Budget): Completable
}