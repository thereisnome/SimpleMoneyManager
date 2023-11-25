package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.AccountRepository

class GetMainAccountUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(): LiveData<Account> {
        return accountRepository.getAccountById(0)
    }
}