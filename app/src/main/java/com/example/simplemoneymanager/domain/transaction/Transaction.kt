package com.example.simplemoneymanager.domain.transaction

import androidx.room.Entity
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import java.util.Date

@Entity(tableName = "transaction_list")
data class Transaction(
    val type: String,
    val name: String,
    val category: Category,
    val amount: String,
    val date: Date,
    val account: Account,
    val id: Int
)