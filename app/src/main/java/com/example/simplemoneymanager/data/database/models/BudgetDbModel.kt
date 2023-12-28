package com.example.simplemoneymanager.data.database.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "budget_list")
data class BudgetDbModel(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long = 0,
    var maxValue: Double,
    @Embedded
    val category: CategoryDbModel
) : Parcelable