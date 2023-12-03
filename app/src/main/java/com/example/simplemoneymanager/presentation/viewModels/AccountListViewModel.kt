package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.domain.account.AccountWithTransactions
import com.example.simplemoneymanager.domain.account.usecases.GetAccountWithTransactionsUseCase
import com.example.simplemoneymanager.domain.account.usecases.GetOverallBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.RemoveAccountUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AccountListViewModel(application: Application): AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val getOverallBalanceUseCase = GetOverallBalanceUseCase(accountRepositoryImpl)
    private val getAccountWithTransactionsUsesCase = GetAccountWithTransactionsUseCase(accountRepositoryImpl)
    private val removeAccountUseCase = RemoveAccountUseCase(accountRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getAccountWithTransactions(): LiveData<List<AccountWithTransactions>> {
        return getAccountWithTransactionsUsesCase()
    }

    fun getOverallBalance(): LiveData<Double>{
        return getOverallBalanceUseCase()
    }

    fun removeAccount(accountId: Long) {
        val disposable = removeAccountUseCase(accountId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Toast.makeText(getApplication(), "Account removed", Toast.LENGTH_LONG).show()
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