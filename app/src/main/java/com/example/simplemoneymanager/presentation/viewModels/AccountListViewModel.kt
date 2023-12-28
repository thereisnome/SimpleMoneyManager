package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.AccountWithTransactionsEntity
import com.example.simplemoneymanager.domain.account.usecases.GetAccountWithTransactionsUseCase
import com.example.simplemoneymanager.domain.account.usecases.GetOverallBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.RemoveAccountUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AccountListViewModel @Inject constructor(
    private val getOverallBalanceUseCase: GetOverallBalanceUseCase,
    private val getAccountWithTransactionsUsesCase: GetAccountWithTransactionsUseCase,
    private val removeAccountUseCase: RemoveAccountUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getAccountWithTransactions(): LiveData<List<AccountWithTransactionsEntity>> {
        return getAccountWithTransactionsUsesCase()
    }

    fun getOverallBalance(): LiveData<Double>{
        return getOverallBalanceUseCase()
    }

    fun removeAccount(accountId: Long) {
        val disposable = removeAccountUseCase(accountId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("AccountListViewModel", "Account removed with id $accountId")
            }, {
                Log.d("VM remove account", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}