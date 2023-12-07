package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import androidx.core.util.Pair
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.common.DateFilter
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListUseCase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class FilterBottomSheetViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val transactionRepositoryImpl = TransactionRepositoryImpl(db.moneyDao())
    private val getTransactionListUseCase = GetTransactionListUseCase(transactionRepositoryImpl)

    fun getTransactionList(): LiveData<List<Transaction>>{
        return getTransactionListUseCase()
    }

    fun convertMillisToDate(millis: Pair<Long, Long>): DateFilter {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val startDate = LocalDate.parse(formatter.format(millis.first))
        val endDate = LocalDate.parse(formatter.format(millis.second))
        return DateFilter(startDate, endDate, true)
    }

    fun createSetOfMonths(transactionList: List<Transaction>): Set<LocalDate> {
        return transactionList.map { LocalDate.of(it.date.year, it.date.month, 1) }.toSet()
    }
}