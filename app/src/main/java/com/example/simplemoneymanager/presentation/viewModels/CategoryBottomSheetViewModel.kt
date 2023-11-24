package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.usecases.GetExpenseCategoryListUseCase
import com.example.simplemoneymanager.domain.category.usecases.GetIncomeCategoryListUseCase
import com.example.simplemoneymanager.domain.category.usecases.RemoveCategoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoryBottomSheetViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val getExpenseCategoryListUseCase = GetExpenseCategoryListUseCase(categoryRepositoryImpl)
    private val getIncomeCategoryListUseCase = GetIncomeCategoryListUseCase(categoryRepositoryImpl)
    private val removeCategoryUseCase = RemoveCategoryUseCase(categoryRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getExpenseCategoryList(): LiveData<List<Category>> {
        return getExpenseCategoryListUseCase.invoke()
    }

    fun getIncomeCategoryList(): LiveData<List<Category>> {
        return getIncomeCategoryListUseCase.invoke()
    }

    fun removeCategory(categoryId: Int){
        val disposable = removeCategoryUseCase(categoryId).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
            Toast.makeText(getApplication(), "Category removed", Toast.LENGTH_LONG).show()
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