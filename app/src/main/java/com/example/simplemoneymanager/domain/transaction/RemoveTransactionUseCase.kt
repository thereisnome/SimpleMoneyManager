package com.example.simplemoneymanager.domain.transaction

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Completable

class RemoveTransactionUseCase(private val moneyDao: MoneyDao) {

    fun removeTransaction(transactionId: Int): Completable{
        return moneyDao.removeTransaction(transactionId)
    }
}