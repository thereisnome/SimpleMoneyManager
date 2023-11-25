package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable

interface TransactionRepository {

    fun addTransaction(transaction: Transaction): Completable

    fun getTransactionList(): LiveData<List<Transaction>>

    fun getIncomeTransactionList(): LiveData<List<Transaction>>

    fun getExpenseTransactionList(): LiveData<List<Transaction>>

    fun getTransactionById(transactionId: Int): LiveData<Transaction>

    fun removeTransaction(transactionId: Long): Completable

    fun removeAllTransactions(): Completable
}