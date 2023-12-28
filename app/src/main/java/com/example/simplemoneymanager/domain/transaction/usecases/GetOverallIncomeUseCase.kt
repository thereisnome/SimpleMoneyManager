package com.example.simplemoneymanager.domain.transaction.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import javax.inject.Inject

class GetOverallIncomeUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    operator fun invoke(): LiveData<Double> {
        return transactionRepository.getOverallIncome()
    }
}