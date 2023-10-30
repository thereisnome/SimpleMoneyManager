package com.example.simplemoneymanager.domain.transaction

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.MoneyDao

class GetTransactionListUseCase(private val moneyDao: MoneyDao) {

    fun getTransactionList(): LiveData<List<Transaction>>{
        return moneyDao.getTransactionList()
    }
}