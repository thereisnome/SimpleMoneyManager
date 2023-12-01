package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.usecases.AddCategoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddCategoryDialogViewModel(application: Application): AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val addCategoryUseCase = AddCategoryUseCase(categoryRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun addCategory(categoryType: Int, categoryName: String, categoryColor: String) {
        val category = Category(categoryType, categoryName, categoryColor = categoryColor)
        val disposable = addCategoryUseCase(category).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            Toast.makeText(getApplication(), "Category added successfully", Toast.LENGTH_LONG)
                .show()
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