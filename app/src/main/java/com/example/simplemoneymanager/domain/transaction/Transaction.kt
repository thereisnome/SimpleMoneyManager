package com.example.simplemoneymanager.domain.transaction

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplemoneymanager.domain.category.Category
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

@Parcelize
@Entity(tableName = "transaction_list")
data class Transaction(
    val type: Int,
    val transactionName: String,
    @Embedded
    val category: Category,
    val amount: Double,
    val transactionAccountId: Long,
    val date: LocalDate,
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long = 0
) : Parcelable {
    companion object{
        const val INCOME = 0
        const val EXPENSE = 1

        fun formatCurrency(value: Double): String {
            val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
            return if (value>=0){
                "+$formattedAmount".replace(" руб.", "₽")
            } else formattedAmount.replace(" руб.", "₽")
        }

    }
}