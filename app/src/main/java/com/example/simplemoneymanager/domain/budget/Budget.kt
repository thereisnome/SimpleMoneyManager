package com.example.simplemoneymanager.domain.budget

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplemoneymanager.domain.category.Category
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "budget_list")
data class Budget(
    var maxValue: Double,
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long = 0,
    @Embedded
    val category: Category
) : Parcelable