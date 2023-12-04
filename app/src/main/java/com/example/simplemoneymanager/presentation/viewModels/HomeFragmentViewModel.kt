package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.account.usecases.GetOverallBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.domain.transaction.usecases.GetCashFlowByMonthUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)

    private val transactionRepositoryImpl = TransactionRepositoryImpl(db.moneyDao())
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val getTransactionListUseCase = GetTransactionListUseCase(transactionRepositoryImpl)
    private val removeTransactionUseCase = RemoveTransactionUseCase(transactionRepositoryImpl)
    private val subtractAccountBalanceUseCase = SubtractAccountBalanceUseCase(accountRepositoryImpl)
    private val getOverallBalanceUseCase = GetOverallBalanceUseCase(accountRepositoryImpl)
    private val getCashFlowByMonthUseCase = GetCashFlowByMonthUseCase(transactionRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getTransactionList(): LiveData<List<Transaction>> {
        return getTransactionListUseCase()
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

    fun getOverallBalance(): LiveData<Double> {
        return getOverallBalanceUseCase()
    }

    fun getMonthIncome(transactionList: List<Transaction>, month: Int): Double {
        return transactionList.filter { it.date.monthValue == month && it.type == Transaction.INCOME }
            .sumOf { it.amount }
    }

    fun getMonthExpense(transactionList: List<Transaction>, month: Int): Double {
        return transactionList.filter { it.date.monthValue == month && it.type == Transaction.EXPENSE }
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