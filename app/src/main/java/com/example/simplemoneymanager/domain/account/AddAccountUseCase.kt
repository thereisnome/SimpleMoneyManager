package com.example.simplemoneymanager.domain.account

import com.example.simplemoneymanager.data.MoneyDao
import com.example.simplemoneymanager.domain.category.Category
import io.reactivex.rxjava3.core.Completable

class AddAccountUseCase(private val moneyDao: MoneyDao) {

    fun addAccount(account: Account): Completable {
        return moneyDao.addAccount(account)
    }
}