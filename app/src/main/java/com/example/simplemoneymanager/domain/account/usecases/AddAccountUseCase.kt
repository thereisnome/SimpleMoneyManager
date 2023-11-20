package com.example.simplemoneymanager.domain.account.usecases

import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.account.Account
import io.reactivex.rxjava3.core.Completable

class AddAccountUseCase(private val moneyDao: MoneyDao) {

    fun addAccount(account: Account): Completable {
        return moneyDao.addAccount(account)
    }
}