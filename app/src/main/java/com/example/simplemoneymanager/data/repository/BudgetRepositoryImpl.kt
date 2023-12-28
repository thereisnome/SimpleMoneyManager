package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.data.mappers.Mapper
import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactionsEntity
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val moneyDao: MoneyDao,
    private val mapper: Mapper
) : BudgetRepository {
    override fun addBudget(budget: BudgetEntity): Completable {
        return moneyDao.addBudget(mapper.mapBudgetEntityToDbModel(budget))
    }

    override fun getBudgetList(): LiveData<List<BudgetEntity>> {
        return moneyDao.getBudgetList().map { budgetDbModelList ->
            budgetDbModelList.map { mapper.mapBudgetDbModelToEntity(it) }
        }
    }

    override fun getBudgetById(budgetId: Long): LiveData<BudgetEntity> {
        return moneyDao.getBudgetById(budgetId).map { budgetDbModel ->
            mapper.mapBudgetDbModelToEntity(budgetDbModel)
        }
    }

    override fun removeBudget(budgetId: Long): Completable {
        return moneyDao.removeBudget(budgetId)
    }

    override fun getBudgetWithTransactions(): LiveData<List<BudgetWithTransactionsEntity>> {
        return moneyDao.getBudgetWithTransactions().map { budgetWithTransactionsDbModelList ->
            budgetWithTransactionsDbModelList.map {
                mapper.mapBudgetWithTransactionsDbModelToEntity(
                    it
                )
            }
        }
    }

    override fun editBudgetById(budget: BudgetEntity): Completable {
        return moneyDao.editBudgetById(budget.budgetId, budget.maxValue, budget.category.id)
    }
}