package com.example.simplemoneymanager.domain.category

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Single

class GetCategoryByIdUseCase(private val moneyDao: MoneyDao) {

    fun getCategoryById(categoryId: Int): Single<Category>{
        return moneyDao.getCategoryById(categoryId)
    }
}