package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.CategoryWithTransactionsEntity
import io.reactivex.rxjava3.core.Completable

interface CategoryRepository {

    fun addCategory(category: CategoryEntity): Completable

    fun getExpenseCategoryList(): LiveData<List<CategoryEntity>>

    fun getIncomeCategoryList(): LiveData<List<CategoryEntity>>

    fun getCategoryList(): LiveData<List<CategoryEntity>>

    fun getCategoryById(categoryId: Int): LiveData<CategoryEntity>

    fun removeCategory(categoryId: Int): Completable

    fun getCategoryWithTransactions(): LiveData<List<CategoryWithTransactionsEntity>>
}