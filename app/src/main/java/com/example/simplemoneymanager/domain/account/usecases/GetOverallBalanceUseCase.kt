package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.AccountRepository

class GetOverallBalanceUseCase(private val accountRepository: AccountRepository) {

    operator fun invoke(): LiveData<Double> {
        return accountRepository.getOverallBalance()
    }
}