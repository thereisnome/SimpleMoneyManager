package com.example.simplemoneymanager.domain.account.usecases

import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AddAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    operator fun invoke(account: AccountEntity): Completable {
        return accountRepository.addAccount(account)
    }
}