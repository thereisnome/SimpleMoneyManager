package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.BudgetRepositoryImpl
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.budget.usecases.AddBudgetUseCase
import com.example.simplemoneymanager.domain.budget.usecases.EditBudgetUseCase
import com.example.simplemoneymanager.domain.budget.usecases.GetBudgetByIdUseCase
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.usecases.GetDefaultCategoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddBudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val budgetUseCaseImpl = BudgetRepositoryImpl(db.moneyDao())
    private val addBudgetUseCase = AddBudgetUseCase(budgetUseCaseImpl)
    private val getDefaultCategoryUseCase = GetDefaultCategoryUseCase(categoryRepositoryImpl)
    private val editBudgetUseCase = EditBudgetUseCase(budgetUseCaseImpl)
    private val getBudgetByIdUseCase = GetBudgetByIdUseCase(budgetUseCaseImpl)

    private val compositeDisposable = CompositeDisposable()

    fun addBudget(maxValue: Double, category: Category) {
        val budget = Budget(maxValue = maxValue, category = category)
        val disposable = addBudgetUseCase(budget).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            }, {
                it.message?.let { it1 -> Log.d("VM Add Budget", it1) }
            })
        compositeDisposable.add(disposable)
    }

    fun getDefaultCategory(): LiveData<Category> {
        return getDefaultCategoryUseCase()
    }

    fun getBudgetById(budgetId: Long): LiveData<Budget>{
        return getBudgetByIdUseCase(budgetId)
    }

    fun editBudget(maxValue: Double, budgetId: Long, category: Category,) {
        val budget = Budget(maxValue, budgetId, category)
        val disposable = editBudgetUseCase(budget).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("VM edit transaction", "Transaction edited: ${budget.budgetId}")
            }, {
                Log.d("VM edit transaction", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}