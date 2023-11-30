package com.example.simplemoneymanager.domain.account

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account_list")
data class Account(
    val accountName: String,
    var balance: Double,
    @PrimaryKey(autoGenerate = true)
    val accountId: Long = 0,
    val accountColor: String
) : Parcelable