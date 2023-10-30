package com.example.simplemoneymanager.domain.account

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.MoneyDao

class GetAccountListUseCase(private val moneyDao: MoneyDao) {

    fun getAccountList(): LiveData<List<Account>>{
        return moneyDao.getAccountList()
    }
}