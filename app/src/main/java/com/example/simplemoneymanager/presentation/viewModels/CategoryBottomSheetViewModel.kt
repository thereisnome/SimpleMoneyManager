package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.usecases.GetExpenseCategoryListUseCase
import com.example.simplemoneymanager.domain.category.usecases.GetIncomeCategoryListUseCase
import com.example.simplemoneymanager.domain.category.usecases.RemoveCategoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CategoryBottomSheetViewModel @Inject constructor(
    private val getExpenseCategoryListUseCase: GetExpenseCategoryListUseCase,
    private val getIncomeCategoryListUseCase: GetIncomeCategoryListUseCase,
    private val removeCategoryUseCase: RemoveCategoryUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getExpenseCategoryList(): LiveData<List<CategoryEntity>> {
        return getExpenseCategoryListUseCase.invoke()
    }

    fun getIncomeCategoryList(): LiveData<List<CategoryEntity>> {
        return getIncomeCategoryListUseCase.invoke()
    }

    fun removeCategory(categoryId: Int){
        val disposable = removeCategoryUseCase(categoryId).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({

        },{
            Log.d("VM remove category", it.message.toString())
        })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}