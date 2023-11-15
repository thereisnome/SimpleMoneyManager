package com.example.simplemoneymanager.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.MoneyDataBase
import com.example.simplemoneymanager.domain.transaction.GetTransactionListUseCase
import com.example.simplemoneymanager.domain.transaction.Transaction

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)
    private val getTransactionListUseCase = GetTransactionListUseCase(db.moneyDao())

    fun getTransactionList(): LiveData<List<Transaction>>{
        return getTransactionListUseCase.getTransactionList()
    }
}