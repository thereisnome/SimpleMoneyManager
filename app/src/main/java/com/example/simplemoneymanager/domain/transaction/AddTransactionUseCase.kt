package com.example.simplemoneymanager.domain.transaction

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Completable

class AddTransactionUseCase(private val moneyDao: MoneyDao) {

    fun addTransaction(transaction: Transaction): Completable {
        return moneyDao.addTransaction(transaction)
    }
}