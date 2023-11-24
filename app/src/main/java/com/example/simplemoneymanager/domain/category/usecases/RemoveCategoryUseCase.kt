package com.example.simplemoneymanager.domain.category.usecases

import com.example.simplemoneymanager.domain.repository.CategoryRepository
import io.reactivex.rxjava3.core.Completable

class RemoveCategoryUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(categoryId: Int): Completable{
        return categoryRepository.removeCategory(categoryId)
    }
}