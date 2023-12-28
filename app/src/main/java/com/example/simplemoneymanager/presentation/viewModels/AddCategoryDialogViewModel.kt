package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.usecases.AddCategoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AddCategoryDialogViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun addCategory(categoryType: Int, categoryName: String, categoryColor: String) {
        val category = CategoryEntity(categoryType, categoryName, categoryColor = categoryColor)
        val disposable = addCategoryUseCase(category).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("AddCategoryDialogViewModel", "Category added $category")
        }, {
            it.message?.let { it1 -> Log.d("VM add category", it1) }
        })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}