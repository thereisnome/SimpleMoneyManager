package com.example.simplemoneymanager.data

import androidx.room.TypeConverter
import com.example.simplemoneymanager.domain.transaction.TransactionList
import com.google.gson.Gson

class MyTypeConverters {

    @TypeConverter
    fun fromTransactionToJSON(transactionList: TransactionList): String{
        return Gson().toJson(transactionList)
    }

    @TypeConverter
    fun fromJSONToTransaction(json: String): TransactionList{
        return Gson().fromJson(json, TransactionList::class.java)
    }
}