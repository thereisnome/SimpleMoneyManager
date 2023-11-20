package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.usecases.GetExpenseCategoryListUseCase
import com.example.simplemoneymanager.domain.category.usecases.GetIncomeCategoryListUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CategoryBottomSheetViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val getExpenseCategoryListUseCase = GetExpenseCategoryListUseCase(categoryRepositoryImpl)
    private val getIncomeCategoryListUseCase = GetIncomeCategoryListUseCase(categoryRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getExpenseCategoryList(): LiveData<List<Category>> {
        return getExpenseCategoryListUseCase.invoke()
    }

    fun getIncomeCategoryList(): LiveData<List<Category>> {
        return getIncomeCategoryListUseCase.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}