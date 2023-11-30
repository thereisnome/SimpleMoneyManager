package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.TransactionRepository

class GetCashFlowByMonthUseCase(private val transactionRepository: TransactionRepository) {

    operator fun invoke(month: String): LiveData<Double> {
        return transactionRepository.getCashFlowByMonth(month)
    }
}