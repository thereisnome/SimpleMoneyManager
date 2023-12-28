package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import javax.inject.Inject

class GetTransactionListUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    operator fun invoke(): LiveData<List<TransactionEntity>> {
        return transactionRepository.getTransactionList()
    }
}