package com.example.simplemoneymanager.presentation.viewModels

import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.common.DateFilter
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListUseCase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

class FilterBottomSheetViewModel @Inject constructor(
    private val getTransactionListUseCase: GetTransactionListUseCase
) : ViewModel() {

    fun getTransactionList(): LiveData<List<TransactionEntity>> {
        return getTransactionListUseCase()
    }

    fun convertMillisToDate(millis: Pair<Long, Long>): DateFilter {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val startDate = LocalDate.parse(formatter.format(millis.first))
        val endDate = LocalDate.parse(formatter.format(millis.second))
        return DateFilter(startDate, endDate, true)
    }

    fun createSetOfMonths(transactionList: List<TransactionEntity>): Set<LocalDate> {
        return transactionList.map {
            LocalDate.of(
                LocalDate.parse(it.date).year,
                LocalDate.parse(it.date).month,
                1
            )
        }.toSet()
    }
}