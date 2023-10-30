package com.example.simplemoneymanager.domain.category

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.MoneyDao
import com.example.simplemoneymanager.domain.category.Category

class GetCategoryListUseCase(private val moneyDao: MoneyDao) {

    fun getCategoryList(): LiveData<List<Category>>{
        return moneyDao.getCategoryList()
    }
}