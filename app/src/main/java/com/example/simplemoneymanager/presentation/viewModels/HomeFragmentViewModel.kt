package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.usecases.GetOverallBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.domain.transaction.usecases.GetCashFlowByMonthUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val getTransactionListUseCase: GetTransactionListUseCase,
    private val removeTransactionUseCase: RemoveTransactionUseCase,
    private val subtractAccountBalanceUseCase: SubtractAccountBalanceUseCase,
    private val getOverallBalanceUseCase: GetOverallBalanceUseCase,
    private val getCashFlowByMonthUseCase: GetCashFlowByMonthUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getTransactionList(): LiveData<List<TransactionEntity>> {
        return getTransactionListUseCase()
    }

    fun removeTransaction(transaction: TransactionEntity) {
        val disposable =
            removeTransactionUseCase.invoke(transaction.transactionId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d("HomeFragmentViewModel", "Transaction removed $transaction")
                }, {
                    it.message?.let { it1 -> Log.d("VM remove transaction", it1) }
                })
        compositeDisposable.add(disposable)
    }

    fun subtractAccountBalance(accountId: Long, amount: Double) {
        val disposable =
            subtractAccountBalanceUseCase(accountId, amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d("HomeFragmentViewModel", "ID: $accountId.accountId, amount: $amount")
                }, {
                    Log.d("HomeFragmentViewModel", it.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    fun getOverallBalance(): LiveData<Double> {
        return getOverallBalanceUseCase()
    }

    fun getMonthIncome(transactionList: List<TransactionEntity>, month: Int): Double {
        return transactionList.filter { LocalDate.parse(it.date).monthValue == month && it.type == TransactionEntity.INCOME }
            .sumOf { it.amount }
    }

    fun getMonthExpense(transactionList: List<TransactionEntity>, month: Int): Double {
        return transactionList.filter { LocalDate.parse(it.date).monthValue == month && it.type == TransactionEntity.EXPENSE }
            .sumOf { it.amount }
    }

    fun getCashFlowByMonth(month: String): LiveData<Double> {
        return getCashFlowByMonthUseCase(month)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}