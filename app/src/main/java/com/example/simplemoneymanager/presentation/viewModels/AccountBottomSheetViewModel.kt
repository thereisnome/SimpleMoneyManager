package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.usecases.GetAccountListUseCase
import com.example.simplemoneymanager.domain.account.usecases.RemoveAccountUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AccountBottomSheetViewModel @Inject constructor (
    private val getAccountListUseCase: GetAccountListUseCase,
    private val removeAccountUseCase: RemoveAccountUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getAccountList(): LiveData<List<AccountEntity>> {
        return getAccountListUseCase()
    }

    fun removeAccount(accountId: Long) {
        val disposable = removeAccountUseCase(accountId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("AccountBottomSheetViewModel", "Account removed by id $accountId")
            }, {
                Log.d("AccountBottomSheetViewModel", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}