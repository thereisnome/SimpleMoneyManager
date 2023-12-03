package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactions
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import io.reactivex.rxjava3.core.Completable

class BudgetRepositoryImpl(private val moneyDao: MoneyDao): BudgetRepository {
    override fun addBudget(budget: Budget): Completable {
        return moneyDao.addBudget(budget)
    }

    override fun getBudgetList(): LiveData<List<Budget>> {
        return moneyDao.getBudgetList()
    }

    override fun getBudgetById(budgetId: Long): LiveData<Budget> {
        return moneyDao.getBudgetById(budgetId)
    }

    override fun removeBudget(budgetId: Long): Completable {
        return moneyDao.removeBudget(budgetId)
    }

    override fun getBudgetWithTransactions(): LiveData<List<BudgetWithTransactions>> {
        return moneyDao.getBudgetWithTransactions()
    }

    override fun editBudgetById(budget: Budget): Completable {
        return moneyDao.editBudgetById(budget.budgetId, budget.maxValue, budget.category.id)
    }
}