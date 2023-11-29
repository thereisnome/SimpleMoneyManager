package com.example.simplemoneymanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction

@TypeConverters(value = [MoneyTypeConverter::class])
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
                val instance = Room.databaseBuilder(context, MoneyDataBase::class.java, DB_NAME)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (1, 'No category', 0, '#E5DEFF')")
                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (0, 'No category', 1, '#E5DEFF')")

                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (1, 'Продукты', 2, '#ad3e1c')")
                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (1, 'Транспорт', 3, '#1c75ad')")
                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (1, 'Развлечения', 4, '#1cad73')")

                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (0, 'Зарплата №1', 5, '#1cad5b')")
                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (0, 'Зарплата №2', 6, '#43ad1c')")
                            db.execSQL("INSERT INTO category_list (categoryType, categoryName, id, categoryColor) VALUES (0, 'Возврат средств', 7, '#ad901c')")

                            db.execSQL("INSERT INTO account_list (accountName, balance, accountId, accountColor) VALUES ('Main account', 0, 0, '#E73D5D')")
                        }
                    }).build()
                db = instance
                return instance
            }
        }
    }

    abstract fun moneyDao(): MoneyDao
}