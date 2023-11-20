package com.example.simplemoneymanager.domain.transaction.usecases

import com.example.simplemoneymanager.domain.repository.TransactionRepository
import io.reactivex.rxjava3.core.Completable

class RemoveTransactionUseCase(private val transactionRepository: TransactionRepository) {

    fun invoke(transactionId: Int): Completable {
        return transactionRepository.removeTransaction(transactionId)
    }
}