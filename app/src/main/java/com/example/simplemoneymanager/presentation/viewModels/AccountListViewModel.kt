package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.account.usecases.GetOverallBalanceUseCase
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListWithAccountsByMonthUseCase

class AccountListViewModel(application: Application): AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)
    private val transactionRepositoryImpl = TransactionRepositoryImpl(db.moneyDao())
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val getTransactionListWithAccountsByMonthUseCase = GetTransactionListWithAccountsByMonthUseCase(transactionRepositoryImpl)
    private val getOverallBalanceUseCase = GetOverallBalanceUseCase(accountRepositoryImpl)

    fun getTransactionListWithAccountsByMonth(month: String): LiveData<Map<Account, List<Transaction>>>{
        return getTransactionListWithAccountsByMonthUseCase(month)
    }

    fun getOverallBalance(): LiveData<Double>{
        return getOverallBalanceUseCase()
    }
}