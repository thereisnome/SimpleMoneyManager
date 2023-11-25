package com.example.simplemoneymanager.domain.account.usecases

import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable

class ClearAccountBalanceUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(account: Account): Completable{
        return accountRepository.clearAccountBalance(account.accountId)
    }

    operator fun invoke(): Completable{
        return accountRepository.clearAllAccountBalances()
    }
}