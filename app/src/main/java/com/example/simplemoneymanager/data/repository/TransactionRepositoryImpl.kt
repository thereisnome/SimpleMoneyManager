package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable

class TransactionRepositoryImpl(private val moneyDao: MoneyDao) : TransactionRepository {

    override fun addTransaction(transaction: Transaction): Completable {
        return moneyDao.addTransaction(transaction)
    }

    override fun getTransactionList(): LiveData<List<Transaction>> {
        return moneyDao.getTransactionList()
    }

    override fun getIncomeTransactionList(): LiveData<List<Transaction>> {
        return moneyDao.getIncomeTransactionList()
    }

    override fun getExpenseTransactionList(): LiveData<List<Transaction>> {
        return moneyDao.getExpenseTransactionList()
    }

    override fun getTransactionById(transactionId: Long): LiveData<Transaction> {
        return moneyDao.getTransactionById(transactionId)
    }

    override fun removeTransaction(transactionId: Long): Completable {
        return moneyDao.removeTransaction(transactionId)
    }

    override fun removeAllTransactions(): Completable {
        return moneyDao.removeAllTransactions()
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

    override fun editTransaction(transaction: Transaction): Completable {
        return moneyDao.editTransactionById(
            transaction.transactionId,
            transaction.type,
            transaction.transactionName,
            transaction.category.id,
            transaction.amount,
            transaction.account.accountId,
            transaction.date
        )
    }
}