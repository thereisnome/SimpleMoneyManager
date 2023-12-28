package com.example.simplemoneymanager.domain.transaction.usecases

import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    operator fun invoke(transaction: TransactionEntity): Completable {
        return transactionRepository.addTransaction(transaction)
    }
}