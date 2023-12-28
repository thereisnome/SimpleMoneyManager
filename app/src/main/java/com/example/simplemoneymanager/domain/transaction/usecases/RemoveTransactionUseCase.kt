package com.example.simplemoneymanager.domain.transaction.usecases

import com.example.simplemoneymanager.domain.repository.TransactionRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RemoveTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    fun invoke(transactionId: Long): Completable {
        return transactionRepository.removeTransaction(transactionId)
    }
}