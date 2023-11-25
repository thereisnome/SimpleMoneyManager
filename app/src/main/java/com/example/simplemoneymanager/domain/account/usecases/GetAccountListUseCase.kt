package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.AccountRepository

class GetAccountListUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(): LiveData<List<Account>> {
        return accountRepository.getAccountList()
    }
}