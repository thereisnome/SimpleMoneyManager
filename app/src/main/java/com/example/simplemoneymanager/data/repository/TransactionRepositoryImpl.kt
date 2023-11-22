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

    override fun getTransactionById(transactionId: Int): LiveData<Transaction> {
        return getTransactionById(transactionId)
    }

    override fun removeTransaction(transactionId: Long): Completable {
        return moneyDao.removeTransaction(transactionId)
    }

    override fun removeAllTransactions(): Completable {
        return moneyDao.removeAllTransactions()
    }
}