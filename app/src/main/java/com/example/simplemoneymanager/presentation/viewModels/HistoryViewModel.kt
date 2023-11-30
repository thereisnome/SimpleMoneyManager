package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.account.usecases.ClearAccountBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.GetOverallBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.domain.transaction.usecases.GetCashFlowByMonthUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.GetOverallExpenseUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.GetOverallIncomeUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveAllTransactionsUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)

    private val transactionRepositoryImpl = TransactionRepositoryImpl(db.moneyDao())
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val getTransactionListUseCase = GetTransactionListUseCase(transactionRepositoryImpl)
    private val removeAllTransactionsUseCase =
        RemoveAllTransactionsUseCase(transactionRepositoryImpl)
    private val removeTransactionUseCase = RemoveTransactionUseCase(transactionRepositoryImpl)
    private val subtractAccountBalanceUseCase = SubtractAccountBalanceUseCase(accountRepositoryImpl)
    private val clearAccountBalanceUseCase = ClearAccountBalanceUseCase(accountRepositoryImpl)
    private val getOverallBalanceUseCase = GetOverallBalanceUseCase(accountRepositoryImpl)
    private val getOverallIncomeUseCase = GetOverallIncomeUseCase(transactionRepositoryImpl)
    private val getOverallExpenseUseCase = GetOverallExpenseUseCase(transactionRepositoryImpl)
    private val getCashFlowByMonthUseCase = GetCashFlowByMonthUseCase(transactionRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getTransactionList(): LiveData<List<Transaction>> {
        return getTransactionListUseCase()
    }

    fun removeAllTransactions() {
        val disposable = removeAllTransactionsUseCase.invoke().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Toast.makeText(getApplication(), "All transactions removed", Toast.LENGTH_SHORT)
                    .show()
            }, {
                Toast.makeText(
                    getApplication(), "Cannot remove all transactions, try again", Toast.LENGTH_LONG
                ).show()
                it.message?.let { it1 -> Log.d("VM remove all transactions", it1) }
            })

        compositeDisposable.add(disposable)
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

    fun clearAllAccountBalances() {
        val disposable = clearAccountBalanceUseCase().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
        compositeDisposable.add(disposable)
    }

    fun subtractAccountBalance(account: Account, amount: Double) {
        val disposable = subtractAccountBalanceUseCase(account, amount).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("VM subtractAccountBalance", "ID: $account.accountId, amount: $amount")
            }, {
                Log.d("VM subtractAccountBalance", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    fun getOverallBalance(): LiveData<Double> {
        return getOverallBalanceUseCase()
    }

    fun getOverallIncome(): LiveData<Double> {
        return getOverallIncomeUseCase()
    }

    fun getOverallExpense(): LiveData<Double> {
        return getOverallExpenseUseCase()
    }

    fun getCashFlowByMonth(month: String): LiveData<Double>{
        return getCashFlowByMonthUseCase(month)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}