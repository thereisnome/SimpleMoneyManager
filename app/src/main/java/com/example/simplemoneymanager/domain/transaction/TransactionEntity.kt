package com.example.simplemoneymanager.domain.transaction

import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.category.CategoryEntity

data class TransactionEntity(
    val type: Int,
    val transactionName: String,
    val category: CategoryEntity,
    val amount: Double,
    val account: AccountEntity,
    val date: String,
    var transactionId: Long = 0
){
    companion object{
        const val INCOME = 0
        const val EXPENSE = 1
    }
}