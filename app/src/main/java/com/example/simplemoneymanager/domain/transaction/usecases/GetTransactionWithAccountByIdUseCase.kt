package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction

class GetTransactionWithAccountByIdUseCase(private val transactionRepository: TransactionRepository) {

    operator fun invoke(transactionId: Long): LiveData<Map<Account, Transaction>> {
        return transactionRepository.getTransactionWithAccountById(transactionId)
    }
}