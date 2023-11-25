package com.example.simplemoneymanager.domain.account.usecases

import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable

class RemoveAccountUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(accountId: Int): Completable{
        return accountRepository.removeAccount(accountId)
    }
}