package com.example.simplemoneymanager.domain.account

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account_list")
data class Account(
    val name: String,
    val balance: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int
) : Parcelable