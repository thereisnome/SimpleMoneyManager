package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.repository.CategoryRepository

class GetIncomeCategoryListUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): LiveData<List<Category>> {
        return categoryRepository.getIncomeCategoryList()
    }
}