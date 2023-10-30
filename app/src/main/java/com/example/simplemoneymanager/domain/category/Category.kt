package com.example.simplemoneymanager.domain.category

import androidx.room.Entity

@Entity(tableName = "category_list")
data class Category(
    val name: String,
    val id: Int
    )