package com.example.simplemoneymanager.domain.transaction.usecases

import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable

class EditTransactionUseCase(private val transactionRepository: TransactionRepository) {

        operator fun invoke(transaction: Transaction): Completable {
            return transactionRepository.editTransaction(transaction)
        }
}