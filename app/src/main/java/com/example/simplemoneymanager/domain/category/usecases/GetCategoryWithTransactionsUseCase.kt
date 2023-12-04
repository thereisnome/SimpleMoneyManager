package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.CategoryWithTransactions
import com.example.simplemoneymanager.domain.repository.CategoryRepository

class GetCategoryWithTransactionsUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(type: Int): LiveData<List<CategoryWithTransactions>>{
        return categoryRepository.getCategoryWithTransactions(type)
    }
}