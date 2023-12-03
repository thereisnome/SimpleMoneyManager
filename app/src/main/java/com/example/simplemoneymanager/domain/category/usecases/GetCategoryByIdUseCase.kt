package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.repository.CategoryRepository

class GetCategoryByIdUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(categoryId: Int): LiveData<Category> {
        return categoryRepository.getCategoryById(categoryId)
    }
}