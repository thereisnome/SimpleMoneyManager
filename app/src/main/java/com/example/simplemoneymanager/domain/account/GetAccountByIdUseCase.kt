package com.example.simplemoneymanager.domain.account

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Single

class GetAccountByIdUseCase(private val moneyDao: MoneyDao) {

    fun getAccountById(accountId: Int): Single<Account>{
        return moneyDao.getAccountById(accountId)
    }
}