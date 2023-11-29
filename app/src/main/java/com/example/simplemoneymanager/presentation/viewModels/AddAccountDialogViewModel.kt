package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.account.usecases.AddAccountUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddAccountDialogViewModel(application: Application): AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val addAccountUseCase = AddAccountUseCase(accountRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun addAccount(accountName: String, accountBalance: Int, accountColor: String) {
        val account = Account(accountName, accountBalance, accountColor=accountColor)
        val disposable = addAccountUseCase(account).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            Toast.makeText(getApplication(), "Account added successfully", Toast.LENGTH_LONG)
                .show()
        }, {
            it.message?.let { it1 -> Log.d("VM add account", it1) }
        })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}