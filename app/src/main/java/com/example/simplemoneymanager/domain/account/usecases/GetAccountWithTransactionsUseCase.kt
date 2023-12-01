package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.AccountWithTransactions
import com.example.simplemoneymanager.domain.repository.AccountRepository

class GetAccountWithTransactionsUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(): LiveData<List<AccountWithTransactions>> {
        return accountRepository.getAccountWithTransactions()
    }
}