package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.data.mappers.Mapper
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import io.reactivex.rxjava3.core.Completable
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val moneyDao: MoneyDao,
    private val mapper: Mapper
) : TransactionRepository {

    override fun addTransaction(transaction: TransactionEntity): Completable {
        return moneyDao.addTransaction(mapper.mapTransactionEntityToDbModel(transaction))
    }

    override fun getTransactionList(): LiveData<List<TransactionEntity>> {
        return moneyDao.getTransactionList().map { transactionDbModelList ->
            transactionDbModelList.map { mapper.mapTransactionDbModelToEntity(it) }
        }
    }

    override fun getIncomeTransactionList(): LiveData<List<TransactionEntity>> {
        return moneyDao.getIncomeTransactionList().map { transactionDbModelList ->
            transactionDbModelList.map { mapper.mapTransactionDbModelToEntity(it) }
        }
    }

    override fun getExpenseTransactionList(): LiveData<List<TransactionEntity>> {
        return moneyDao.getExpenseTransactionList().map { transactionDbModelList ->
            transactionDbModelList.map { mapper.mapTransactionDbModelToEntity(it) }
        }
    }

    override fun getTransactionById(transactionId: Long): LiveData<TransactionEntity> {
        return moneyDao.getTransactionById(transactionId).map { transactionDbModel ->
            mapper.mapTransactionDbModelToEntity(transactionDbModel)
        }
    }

    override fun removeTransaction(transactionId: Long): Completable {
        return moneyDao.removeTransaction(transactionId)
    }

    override fun getCashFlowByMonth(month: String): LiveData<Double> {
        return moneyDao.getCashFlowByMonth(month)
    }

    override fun getOverallIncome(): LiveData<Double> {
        return moneyDao.getOverallIncome()
    }

    override fun getOverallExpense(): LiveData<Double> {
        return moneyDao.getOverallExpense()
    }

    override fun editTransaction(transaction: TransactionEntity): Completable {
        return moneyDao.editTransactionById(
            transaction.transactionId,
            transaction.type,
            transaction.transactionName,
            transaction.category.id,
            transaction.amount,
            transaction.account.accountId,
            LocalDate.parse(transaction.date)
        )
    }
}