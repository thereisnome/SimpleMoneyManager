package com.example.simplemoneymanager.domain.category

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Completable

class RemoveCategoryUseCase(private val moneyDao: MoneyDao) {

    fun removeCategory(categoryId: Int): Completable{
        return moneyDao.removeCategory(categoryId)
    }
}