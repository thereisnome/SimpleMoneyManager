package com.example.simplemoneymanager.domain.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplemoneymanager.domain.category.Category

@Entity(tableName = "transaction_list")
data class Transaction(
//    val type: Int,
    val name: String,
//    val category: Category,
    val amount: Int,
//    val account: Account,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)