package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.account.Account

class GetAccountByIdUseCase(private val moneyDao: MoneyDao) {

    fun getAccountById(accountId: Int): LiveData<Account> {
        return moneyDao.getAccountById(accountId)
    }
}