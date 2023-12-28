package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import io.reactivex.rxjava3.core.Completable

interface TransactionRepository {

    fun addTransaction(transaction: TransactionEntity): Completable

    fun getTransactionList(): LiveData<List<TransactionEntity>>

    fun getIncomeTransactionList(): LiveData<List<TransactionEntity>>

    fun getExpenseTransactionList(): LiveData<List<TransactionEntity>>

    fun getTransactionById(transactionId: Long): LiveData<TransactionEntity>

    fun removeTransaction(transactionId: Long): Completable

    fun getCashFlowByMonth(month: String): LiveData<Double>

    fun getOverallIncome(): LiveData<Double>

    fun getOverallExpense(): LiveData<Double>

    fun editTransaction(transaction: TransactionEntity): Completable
}