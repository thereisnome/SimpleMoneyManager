package com.example.simplemoneymanager.domain.category

data class CategoryEntity(
    val categoryType: Int,
    val categoryName: String,
    val id: Int = 0,
    val categoryColor: String
){
    companion object{
        const val INCOME = 0
        const val EXPENSE = 1
    }
}