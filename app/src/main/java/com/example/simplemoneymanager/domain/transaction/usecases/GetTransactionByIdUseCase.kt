package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction

class GetTransactionByIdUseCase(private val transactionRepository: TransactionRepository) {

    operator fun invoke(transactionId: Long): LiveData<Transaction> {
        return transactionRepository.getTransactionById(transactionId)
    }
}