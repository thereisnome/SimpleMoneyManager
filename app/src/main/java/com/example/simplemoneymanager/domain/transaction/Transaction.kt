package com.example.simplemoneymanager.domain.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import java.time.LocalDate

@Entity(tableName = "transaction_list")
data class Transaction(
    val type: Int,
    val name: String,
    val category: Category,
    val amount: Int,
    val account: Account,
    val date: LocalDate,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)