package com.example.simplemoneymanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MoneyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: Account): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transaction: Transaction): Completable

    @Query("SELECT * FROM category_list")
    fun getCategoryList(): LiveData<List<Category>>

    @Query("SELECT * FROM account_list")
    fun getAccountList(): LiveData<List<Account>>

    @Query("SELECT * FROM transaction_list")
    fun getTransactionList(): LiveData<List<Transaction>>

    @Query("SELECT * FROM account_list WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): Single<Category>

    @Query("SELECT * FROM account_list WHERE id = :accountId")
    fun getAccountById(accountId: Int): Single<Account>

    @Query("SELECT * FROM account_list WHERE id = :transactionId")
    fun getTransactionById(transactionId: Int): Single<Transaction>

    @Query("DELETE FROM category_list WHERE id = :categoryId")
    fun removeCategory(categoryId: Int): Completable

    @Query("DELETE FROM account_list WHERE id = :accountId")
    fun removeAccount(accountId: Int): Completable

    @Query("DELETE FROM transaction_list WHERE id = :transactionId")
    fun removeTransaction(transactionId: Int): Completable
}