package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import com.example.simplemoneymanager.domain.transaction.Transaction

class GetTransactionListWithAccountsByMonthUseCase(private val transactionRepository: TransactionRepository) {

    operator fun invoke(month: String): LiveData<Map<Account, List<Transaction>>> {
        return transactionRepository.getTransactionListWithAccountsByMonth(month)
    }
}