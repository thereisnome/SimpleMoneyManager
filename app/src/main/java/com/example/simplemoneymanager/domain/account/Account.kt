package com.example.simplemoneymanager.domain.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_list")
data class Account(
    val name: String,
    val balance: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)