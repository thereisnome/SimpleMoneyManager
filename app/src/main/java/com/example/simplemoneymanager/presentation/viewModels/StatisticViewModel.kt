package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.domain.category.CategoryWithTransactions
import com.example.simplemoneymanager.domain.category.usecases.GetCategoryWithTransactionsUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.time.LocalDate

class StatisticViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val getCategoryWithTransactionsUseCase = GetCategoryWithTransactionsUseCase(categoryRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getCategoryWithTransactions(): LiveData<List<CategoryWithTransactions>>{
        return getCategoryWithTransactionsUseCase()
    }

    fun filterTransactionsByDate(
        startDay: LocalDate,
        endDay: LocalDate,
        categoryWithTransactionsList: List<CategoryWithTransactions>
    ): List<CategoryWithTransactions> {
        val result = mutableListOf<CategoryWithTransactions>()
        categoryWithTransactionsList.forEach { categoryWithTransactions ->
            val item = CategoryWithTransactions(
                categoryWithTransactions.category,
                categoryWithTransactions.transactions.filter { it.date in startDay..endDay })
            result.add(item)
        }
        return result.filter { it.transactions.isNotEmpty() }
    }

    fun filterTransactionsByDate(categoryWithTransactionsList: List<CategoryWithTransactions>): List<CategoryWithTransactions> {
        val result = mutableListOf<CategoryWithTransactions>()
        categoryWithTransactionsList.forEach { categoryWithTransactions ->
            val item = CategoryWithTransactions(
                categoryWithTransactions.category,
                categoryWithTransactions.transactions.filter { it.date.monthValue == LocalDate.now().monthValue })
            result.add(item)
        }
        return result.filter { it.transactions.isNotEmpty() }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}