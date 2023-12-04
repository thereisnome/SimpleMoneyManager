package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.domain.category.CategoryWithTransactions
import com.example.simplemoneymanager.domain.category.usecases.GetCategoryWithTransactionsUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class StatisticViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val getCategoryWithTransactionsUseCase = GetCategoryWithTransactionsUseCase(categoryRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getCategoryWithTransactions(type: Int): LiveData<List<CategoryWithTransactions>>{
        return getCategoryWithTransactionsUseCase(type)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}