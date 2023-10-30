package com.example.simplemoneymanager.domain.account

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Completable

class RemoveAccountUseCase(private val moneyDao: MoneyDao) {

    fun removeAccount(accountId: Int): Completable{
        return moneyDao.removeAccount(accountId)
    }
}