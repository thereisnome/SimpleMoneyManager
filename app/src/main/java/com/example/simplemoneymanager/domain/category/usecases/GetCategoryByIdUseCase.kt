package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    operator fun invoke(categoryId: Int): LiveData<CategoryEntity> {
        return categoryRepository.getCategoryById(categoryId)
    }
}