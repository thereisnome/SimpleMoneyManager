package com.example.simplemoneymanager.domain.account.usecases

import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RemoveAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    operator fun invoke(accountId: Long): Completable{
        return accountRepository.removeAccount(accountId)
    }
}