package com.example.simplemoneymanager.domain.account.usecases

import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable

class SubtractAccountBalanceUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(accountId: Long, amount: Double): Completable{
        return accountRepository.subtractAccountBalance(accountId, amount)
    }
}