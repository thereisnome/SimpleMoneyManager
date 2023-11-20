package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import io.reactivex.rxjava3.core.Completable

class CategoryRepositoryImpl(private val moneyDao: MoneyDao): CategoryRepository {


    override fun addCategory(category: Category): Completable {
        return moneyDao.addCategory(category)
    }

    override fun getExpenseCategoryList(): LiveData<List<Category>> {
        return moneyDao.getExpenseCategoryList()
    }

    override fun getIncomeCategoryList(): LiveData<List<Category>> {
        return moneyDao.getIncomeCategoryList()
    }

    override fun getCategoryById(categoryId: Int): LiveData<Category> {
        return moneyDao.getCategoryById(categoryId)
    }

    override fun removeCategory(categoryId: Int): Completable {
        return moneyDao.removeCategory(categoryId)
    }
}