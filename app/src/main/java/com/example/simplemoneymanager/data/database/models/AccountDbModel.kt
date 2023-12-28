package com.example.simplemoneymanager.data.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account_list")
data class AccountDbModel(
    @PrimaryKey(autoGenerate = true)
    val accountId: Long = 0,
    val accountName: String,
    var balance: Double,
    val accountColor: String
) : Parcelable