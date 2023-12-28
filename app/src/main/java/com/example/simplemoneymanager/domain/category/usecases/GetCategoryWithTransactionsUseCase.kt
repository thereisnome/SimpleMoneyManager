package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.CategoryWithTransactionsEntity
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryWithTransactionsUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): LiveData<List<CategoryWithTransactionsEntity>>{
        return categoryRepository.getCategoryWithTransactions()
    }
}