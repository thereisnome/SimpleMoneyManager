package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.category.CategoryWithTransactions
import com.example.simplemoneymanager.domain.category.usecases.GetCategoryWithTransactionsUseCase
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate

class StatisticViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val transactionRepositoryImpl = TransactionRepositoryImpl(db.moneyDao())
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val getCategoryWithTransactionsUseCase =
        GetCategoryWithTransactionsUseCase(categoryRepositoryImpl)
    private val removeTransactionUseCase = RemoveTransactionUseCase(transactionRepositoryImpl)
    private val subtractAccountBalanceUseCase = SubtractAccountBalanceUseCase(accountRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getCategoryWithTransactions(): LiveData<List<CategoryWithTransactions>> {
        return getCategoryWithTransactionsUseCase()
    }

    fun filterCategoryWithTransactionsByDate(
        startDay: LocalDate,
        endDay: LocalDate,
        categoryWithTransactionsList: List<CategoryWithTransactions>
    ): List<CategoryWithTransactions> {
        val result = mutableListOf<CategoryWithTransactions>()
        categoryWithTransactionsList.forEach { categoryWithTransactions ->
            val item = CategoryWithTransactions(
                categoryWithTransactions.category,
                categoryWithTransactions.transactions.filter { it.date in startDay..endDay })
            result.add(item)
        }
        return result.filter { it.transactions.isNotEmpty() }
    }

    fun filterTransactionsByDate(
        startDay: LocalDate,
        endDay: LocalDate, transactionList: List<Transaction>
    ): List<Transaction> {
        return transactionList.filter { it.date in startDay..endDay }
    }

    fun removeTransaction(transaction: Transaction) {
        val disposable =
            removeTransactionUseCase.invoke(transaction.transactionId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Toast.makeText(getApplication(), "Transaction removed", Toast.LENGTH_SHORT)
                        .show()
                }, {
                    Toast.makeText(
                        getApplication(), "Cannot remove transaction, try again", Toast.LENGTH_LONG
                    ).show()
                    it.message?.let { it1 -> Log.d("VM remove transaction", it1) }
                })
        compositeDisposable.add(disposable)
    }

    fun subtractAccountBalance(accountId: Long, amount: Double) {
        val disposable =
            subtractAccountBalanceUseCase(accountId, amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d("VM subtractAccountBalance", "ID: $accountId.accountId, amount: $amount")
                }, {
                    Log.d("VM subtractAccountBalance", it.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}