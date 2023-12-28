package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.domain.budget.usecases.AddBudgetUseCase
import com.example.simplemoneymanager.domain.budget.usecases.EditBudgetUseCase
import com.example.simplemoneymanager.domain.budget.usecases.GetBudgetByIdUseCase
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.usecases.GetDefaultCategoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AddBudgetViewModel @Inject constructor(
    private val addBudgetUseCase: AddBudgetUseCase,
    private val getDefaultCategoryUseCase: GetDefaultCategoryUseCase,
    private val editBudgetUseCase: EditBudgetUseCase,
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun addBudget(maxValue: Double, category: CategoryEntity) {
        val budget = BudgetEntity(maxValue = maxValue, category = category)
        val disposable = addBudgetUseCase(budget).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("AddBudgetViewModel", "Budget added $budget")
            }, {
                it.message?.let { it1 -> Log.d("VM Add Budget", it1) }
            })
        compositeDisposable.add(disposable)
    }

    fun getDefaultCategory(): LiveData<CategoryEntity> {
        return getDefaultCategoryUseCase()
    }

    fun getBudgetById(budgetId: Long): LiveData<BudgetEntity>{
        return getBudgetByIdUseCase(budgetId)
    }

    fun editBudget(maxValue: Double, budgetId: Long, category: CategoryEntity) {
        val budget = BudgetEntity(maxValue, budgetId, category)
        val disposable = editBudgetUseCase(budget).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("AddBudgetViewModel", "Budget edited: $budget")
            }, {
                Log.d("AddBudgetViewModel", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}