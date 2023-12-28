package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.category.CategoryWithTransactionsEntity
import com.example.simplemoneymanager.domain.category.usecases.GetCategoryWithTransactionsUseCase
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate
import javax.inject.Inject

class StatisticViewModel  @Inject constructor(
    private val getCategoryWithTransactionsUseCase: GetCategoryWithTransactionsUseCase,
    private val removeTransactionUseCase: RemoveTransactionUseCase,
    private val subtractAccountBalanceUseCase: SubtractAccountBalanceUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getCategoryWithTransactions(): LiveData<List<CategoryWithTransactionsEntity>> {
        return getCategoryWithTransactionsUseCase()
    }

    fun filterCategoryWithTransactionsByDate(
        startDay: LocalDate,
        endDay: LocalDate,
        categoryWithTransactionsList: List<CategoryWithTransactionsEntity>
    ): List<CategoryWithTransactionsEntity> {
        val result = mutableListOf<CategoryWithTransactionsEntity>()
        categoryWithTransactionsList.forEach { categoryWithTransactions ->
            val item = CategoryWithTransactionsEntity(
                categoryWithTransactions.category,
                categoryWithTransactions.transactions.filter { LocalDate.parse(it.date) in startDay..endDay })
            result.add(item)
        }
        return result.filter { it.transactions.isNotEmpty() }
    }

    fun filterTransactionsByDate(
        startDay: LocalDate,
        endDay: LocalDate, transactionList: List<TransactionEntity>
    ): List<TransactionEntity> {
        return transactionList.filter { LocalDate.parse(it.date) in startDay..endDay }
    }

    fun removeTransaction(transaction: TransactionEntity) {
        val disposable =
            removeTransactionUseCase.invoke(transaction.transactionId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d("StatisticViewModel", "Transaction removed $transaction")
                }, {
                    it.message?.let { it1 -> Log.d("StatisticViewModel", it1) }
                })
        compositeDisposable.add(disposable)
    }

    fun subtractAccountBalance(accountId: Long, amount: Double) {
        val disposable =
            subtractAccountBalanceUseCase(accountId, amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d("StatisticViewModel", "ID: $accountId.accountId, amount: $amount")
                }, {
                    Log.d("StatisticViewModel", it.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}