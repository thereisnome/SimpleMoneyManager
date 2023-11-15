package com.example.simplemoneymanager.domain.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_list")
data class Category(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int
    )