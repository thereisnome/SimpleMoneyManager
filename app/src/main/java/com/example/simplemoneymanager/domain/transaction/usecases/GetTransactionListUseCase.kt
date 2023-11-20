package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction

class GetTransactionListUseCase(private val transactionRepository: TransactionRepository) {

    operator fun invoke(): LiveData<List<Transaction>> {
        return transactionRepository.getTransactionList()
    }
}