package com.example.simplemoneymanager.domain.transaction.usecases

import com.example.simplemoneymanager.domain.repository.TransactionRepository
import io.reactivex.rxjava3.core.Completable

class RemoveAllTransactionsUseCase(private val transactionRepository: TransactionRepository) {

    fun invoke(): Completable {
        return transactionRepository.removeAllTransactions()
    }
}