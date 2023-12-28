package com.example.simplemoneymanager.domain.category.usecases

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class GetIncomeCategoryListUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): LiveData<List<CategoryEntity>> {
        return categoryRepository.getIncomeCategoryList()
    }
}