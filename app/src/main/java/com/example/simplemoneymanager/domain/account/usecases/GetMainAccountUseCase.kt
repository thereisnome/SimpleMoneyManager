package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.repository.AccountRepository
import javax.inject.Inject

class GetMainAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    operator fun invoke(): LiveData<AccountEntity> {
        return accountRepository.getAccountById(0)
    }
}