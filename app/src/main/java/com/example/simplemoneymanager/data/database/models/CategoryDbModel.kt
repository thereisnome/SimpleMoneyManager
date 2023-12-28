package com.example.simplemoneymanager.data.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "category_list")
data class CategoryDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryType: Int,
    val categoryName: String,
    val categoryColor: String
) : Parcelable {
    companion object{
        const val INCOME = 0
        const val EXPENSE = 1
    }
}