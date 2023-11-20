package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.account.Account

class GetAccountListUseCase(private val moneyDao: MoneyDao) {

    fun getAccountList(): LiveData<List<Account>> {
        return moneyDao.getAccountList()
    }
}