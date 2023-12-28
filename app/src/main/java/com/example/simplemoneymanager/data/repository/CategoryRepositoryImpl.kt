package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.data.mappers.Mapper
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.CategoryWithTransactionsEntity
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val moneyDao: MoneyDao,
    private val mapper: Mapper
) : CategoryRepository {


    override fun addCategory(category: CategoryEntity): Completable {
        return moneyDao.addCategory(mapper.mapCategoryEntityToDbModel(category))
    }

    override fun getExpenseCategoryList(): LiveData<List<CategoryEntity>> {
        return moneyDao.getExpenseCategoryList().map { categoryDbModelList ->
            categoryDbModelList.map { mapper.mapCategoryDbModelToEntity(it) }
        }
    }

    override fun getIncomeCategoryList(): LiveData<List<CategoryEntity>> {
        return moneyDao.getIncomeCategoryList().map { categoryDbModelList ->
            categoryDbModelList.map { mapper.mapCategoryDbModelToEntity(it) }
        }
    }

    override fun getCategoryList(): LiveData<List<CategoryEntity>> {
        return moneyDao.getCategoryList().map { categoryDbModelList ->
            categoryDbModelList.map { mapper.mapCategoryDbModelToEntity(it) }
        }
    }

    override fun getCategoryById(categoryId: Int): LiveData<CategoryEntity> {
        return moneyDao.getCategoryById(categoryId).map { categoryDbModel ->
            mapper.mapCategoryDbModelToEntity(categoryDbModel)
        }
    }

    override fun removeCategory(categoryId: Int): Completable {
        return moneyDao.removeCategory(categoryId)
    }

    override fun getCategoryWithTransactions(): LiveData<List<CategoryWithTransactionsEntity>> {
        return moneyDao.getCategoryWithTransactions().map { categoryWithTransactionsDbModelList ->
            categoryWithTransactionsDbModelList.map {
                mapper.mapCategoryWithTransactionsDbModelToEntity(
                    it
                )
            }
        }
    }
}