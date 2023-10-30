package com.example.simplemoneymanager.domain.transaction

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Single

class GetTransactionByIdUseCase(private val moneyDao: MoneyDao) {

    fun getTransactionById(transactionId: Int): Single<Transaction>{
        return moneyDao.getTransactionById(transactionId)
    }
}