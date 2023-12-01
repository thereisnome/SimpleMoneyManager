package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.domain.account.AccountWithTransactions
import com.example.simplemoneymanager.domain.account.usecases.GetAccountWithTransactionsUseCase
import com.example.simplemoneymanager.domain.account.usecases.GetOverallBalanceUseCase

class AccountListViewModel(application: Application): AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val getOverallBalanceUseCase = GetOverallBalanceUseCase(accountRepositoryImpl)
    private val getAccountWithTransactionsUsesCase = GetAccountWithTransactionsUseCase(accountRepositoryImpl)

    fun getAccountWithTransactions(): LiveData<List<AccountWithTransactions>> {
        return getAccountWithTransactionsUsesCase()
    }

    fun getOverallBalance(): LiveData<Double>{
        return getOverallBalanceUseCase()
    }
}