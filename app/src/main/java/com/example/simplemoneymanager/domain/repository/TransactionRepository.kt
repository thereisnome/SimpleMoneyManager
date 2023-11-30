package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable

interface TransactionRepository {

    fun addTransaction(transaction: Transaction): Completable

    fun getTransactionList(): LiveData<List<Transaction>>

    fun getIncomeTransactionList(): LiveData<List<Transaction>>

    fun getExpenseTransactionList(): LiveData<List<Transaction>>

    fun getTransactionById(transactionId: Long): LiveData<Transaction>

    fun removeTransaction(transactionId: Long): Completable

    fun removeAllTransactions(): Completable

    fun getCashFlowByMonth(month: String): LiveData<Double>

    fun getOverallIncome(): LiveData<Double>

    fun getOverallExpense(): LiveData<Double>

    fun editTransaction(transaction: Transaction): Completable
}