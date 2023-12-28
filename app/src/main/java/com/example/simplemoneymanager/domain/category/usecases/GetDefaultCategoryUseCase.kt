package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class GetDefaultCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): LiveData<CategoryEntity> {
        return categoryRepository.getCategoryById(0)
    }
}