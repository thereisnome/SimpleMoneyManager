package com.example.simplemoneymanager.domain.category

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "category_list")
data class Category(
    val categoryType: Int,
    val categoryName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {
    companion object{
        const val INCOME = 0
        const val EXPENSE = 1
        val DEFAULT_CATEGORY = Category(INCOME, "No Category", -1)
    }
}