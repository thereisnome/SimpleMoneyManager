package com.example.simplemoneymanager.data.database.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Entity(tableName = "transaction_list", foreignKeys = [ForeignKey(entity = AccountDbModel::class, parentColumns = ["accountId"], childColumns = ["accountId"], onDelete = CASCADE)])
data class TransactionDbModel(
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long = 0,
    val type: Int,
    val transactionName: String,
    @Embedded
    val category: CategoryDbModel,
    val amount: Double,
    @Embedded
    val account: AccountDbModel,
    val date: LocalDate
) : Parcelable {
    companion object{
        const val INCOME = 0
        const val EXPENSE = 1
    }
}