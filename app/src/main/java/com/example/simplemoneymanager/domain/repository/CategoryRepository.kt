package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.CategoryWithTransactions
import io.reactivex.rxjava3.core.Completable

interface CategoryRepository {

    fun addCategory(category: Category): Completable

    fun getExpenseCategoryList(): LiveData<List<Category>>

    fun getIncomeCategoryList(): LiveData<List<Category>>

    fun getCategoryList(): LiveData<List<Category>>

    fun getCategoryById(categoryId: Int): LiveData<Category>

    fun removeCategory(categoryId: Int): Completable

    fun getCategoryWithTransactions(): LiveData<List<CategoryWithTransactions>>
}