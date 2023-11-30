package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable

interface TransactionRepository {

    fun addTransaction(transaction: Transaction): Completable

    fun getTransactionList(): LiveData<List<Transaction>>

    fun getIncomeTransactionList(): LiveData<List<Transaction>>

    fun getExpenseTransactionList(): LiveData<List<Transaction>>

    fun getTransactionListByMonth(month: String): LiveData<List<Transaction>>

    fun getTransactionListWithAccountsByMonth(month: String): LiveData<Map<Account, List<Transaction>>>

    fun getTransactionWithAccountById(transactionId: Long): LiveData<Map<Account, Transaction>>

    fun getTransactionAccountMap(): LiveData<Map<Account, Transaction>>

    fun getTransactionById(transactionId: Long): LiveData<Transaction>

    fun removeTransaction(transactionId: Long): Completable

    fun removeAllTransactions(): Completable

    fun getCashFlowByMonth(month: String): LiveData<Double>

    fun getOverallIncome(): LiveData<Double>

    fun getOverallExpense(): LiveData<Double>

    fun editTransaction(transaction: Transaction): Completable
}