package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.Category
import io.reactivex.rxjava3.core.Completable

interface CategoryRepository {

    fun addCategory(category: Category): Completable

    fun getExpenseCategoryList(): LiveData<List<Category>>

    fun getIncomeCategoryList(): LiveData<List<Category>>

    fun getCategoryById(categoryId: Int): LiveData<Category>

    fun removeCategory(categoryId: Int): Completable
}