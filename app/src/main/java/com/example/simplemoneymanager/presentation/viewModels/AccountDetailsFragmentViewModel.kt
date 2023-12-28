package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.usecases.GetAccountByIdUseCase
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AccountDetailsFragmentViewModel @Inject constructor(
    private val removeTransactionUseCase: RemoveTransactionUseCase,
    private val subtractAccountBalanceUseCase: SubtractAccountBalanceUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val getTransactionListUseCase: GetTransactionListUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getAccountById(accountId: Long): LiveData<AccountEntity>{
        return getAccountByIdUseCase(accountId)
    }

    fun getTransactionList(): LiveData<List<TransactionEntity>>{
        return getTransactionListUseCase()
    }

    fun removeTransaction(transaction: TransactionEntity) {
        val disposable =
            removeTransactionUseCase.invoke(transaction.transactionId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({

                }, {
                    it.message?.let { it1 -> Log.d("VM remove transaction", it1) }
                })
        compositeDisposable.add(disposable)
    }

    fun subtractAccountBalance(accountId: Long, amount: Double) {
        val disposable = subtractAccountBalanceUseCase(accountId, amount).subscribeOn(Schedulers.io())
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