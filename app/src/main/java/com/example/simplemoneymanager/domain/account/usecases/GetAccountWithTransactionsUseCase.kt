package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.AccountWithTransactionsEntity
import com.example.simplemoneymanager.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountWithTransactionsUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    operator fun invoke(): LiveData<List<AccountWithTransactionsEntity>> {
        return accountRepository.getAccountWithTransactions()
    }
}