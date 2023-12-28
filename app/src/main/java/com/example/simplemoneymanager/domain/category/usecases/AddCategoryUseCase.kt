package com.example.simplemoneymanager.domain.category.usecases

import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    operator fun invoke(category: CategoryEntity): Completable {
        return categoryRepository.addCategory(category)
    }
}