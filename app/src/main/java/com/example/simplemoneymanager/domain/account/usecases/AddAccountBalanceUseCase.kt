package com.example.simplemoneymanager.domain.account.usecases

import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable

class AddAccountBalanceUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(account: Account, amount: Double): Completable{
        return accountRepository.addAccountBalance(account.accountId, amount)
    }
}