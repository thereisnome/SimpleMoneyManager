package com.example.simplemoneymanager.domain.category

import com.example.simplemoneymanager.data.MoneyDao
import io.reactivex.rxjava3.core.Completable

class AddCategoryUseCase(private val moneyDao: MoneyDao) {

    fun addCategory(category: Category): Completable {
        return moneyDao.addCategory(category)
    }
}