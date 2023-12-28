package com.example.simplemoneymanager.domain.account.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.AccountRepository
import javax.inject.Inject

class GetOverallBalanceUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    operator fun invoke(): LiveData<Double> {
        return accountRepository.getOverallBalance()
    }
}