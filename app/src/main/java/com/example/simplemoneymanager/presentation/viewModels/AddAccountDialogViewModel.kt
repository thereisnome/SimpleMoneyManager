package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.usecases.AddAccountUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AddAccountDialogViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun addAccount(accountName: String, balance: Double, accountColor: String) {
        val account =
            AccountEntity(accountName = accountName, balance = balance, accountColor = accountColor)
        val disposable = addAccountUseCase(account).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("AddAccountDialogViewModel", "Account added $account")
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