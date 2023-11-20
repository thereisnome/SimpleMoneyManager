package com.example.simplemoneymanager.domain.transaction.usecases

import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable

class AddTransactionUseCase(private val transactionRepository: TransactionRepository) {

    fun invoke(transaction: Transaction): Completable {
        return transactionRepository.addTransaction(transaction)
    }
}