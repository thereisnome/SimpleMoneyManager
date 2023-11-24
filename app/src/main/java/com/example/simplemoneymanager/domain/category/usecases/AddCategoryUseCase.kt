package com.example.simplemoneymanager.domain.category.usecases

import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import io.reactivex.rxjava3.core.Completable

class AddCategoryUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(category: Category): Completable {
        return categoryRepository.addCategory(category)
    }
}