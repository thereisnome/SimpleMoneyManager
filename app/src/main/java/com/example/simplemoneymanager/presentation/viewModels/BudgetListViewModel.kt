package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactionsEntity
import com.example.simplemoneymanager.domain.budget.usecases.GetBudgetWithTransactionsUseCase
import com.example.simplemoneymanager.domain.budget.usecases.RemoveBudgetUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class BudgetListViewModel @Inject constructor(
    private val getBudgetWithTransactionsUseCase: GetBudgetWithTransactionsUseCase,
    private val removeBudgetUseCase: RemoveBudgetUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getBudgetWithTransactions(): LiveData<List<BudgetWithTransactionsEntity>> {
        return getBudgetWithTransactionsUseCase()
    }

    fun removeBudget(budgetId: Long){
        val disposable = removeBudgetUseCase(budgetId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("BudgetListViewModel", "Budget removed by id $budgetId")
        }, {
            Log.d("BudgetListViewModel", it.message.toString())
        })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}