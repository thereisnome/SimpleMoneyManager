package com.example.simplemoneymanager.domain.transaction

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplemoneymanager.domain.category.Category
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Entity(tableName = "transaction_list")
data class Transaction(
    val type: Int,
    val transactionName: String,
    @Embedded
    val category: Category,
    val amount: Int,
//    val account: Account,
    val date: LocalDate,
    @PrimaryKey(autoGenerate = true)
    var transactionId: Int = 0
) : Parcelable {

    companion object{
        const val INCOME = 0
        const val EXPENSE = 1
    }
}