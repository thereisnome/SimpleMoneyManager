package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction

class GetTransactionByIdUseCase(private val transactionRepository: TransactionRepository) {

    fun invoke(transactionId: Int): LiveData<Transaction> {
        return transactionRepository.getTransactionById(transactionId)
    }
}