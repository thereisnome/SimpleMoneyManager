package com.example.simplemoneymanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction

@TypeConverters(value = [MyTypeConverters::class])
@Database(
    entities = [Transaction::class, Category::class, Account::class],
    version = 1,
    exportSchema = false
)
abstract class MoneyDataBase : RoomDatabase() {

    companion object {
        private var db: MoneyDataBase? = null
        private const val DB_NAME = "Money DB"
        private val LOCK = Any()

        fun getInstance(context: Context): MoneyDataBase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(context, MoneyDataBase::class.java, DB_NAME).build()
                db = instance
                return instance
            }
        }
    }

    abstract fun moneyDao(): MoneyDao
}