package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.repository.CategoryRepository

class GetDefaultCategoryUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): LiveData<Category> {
        return categoryRepository.getCategoryById(0)
    }
}