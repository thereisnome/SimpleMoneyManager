package com.example.simplemoneymanager.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simplemoneymanager.domain.account.AccountWithTransactions
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.core.Completable
import java.time.LocalDate

@Dao
interface MoneyDao {

//    Category

    @Query("DELETE FROM category_list WHERE id = :categoryId")
    fun removeCategory(categoryId: Int): Completable

    @Query("SELECT * FROM category_list WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): LiveData<Category>

    @Query("SELECT * FROM category_list WHERE categoryType = 1")
    fun getExpenseCategoryList(): LiveData<List<Category>>

    @Query("SELECT * FROM category_list WHERE categoryType = 0")
    fun getIncomeCategoryList(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category): Completable

//    Transaction

    @Query("UPDATE transaction_list SET type = :type, transactionName = :transactionName, amount = :amount, date = :date, id = :categoryId, accountId = :transactionAccountId WHERE transactionId = :transactionId")
    fun editTransactionById(
        transactionId: Long,
        type: Int,
        transactionName: String,
        categoryId: Int,
        amount: Double,
        transactionAccountId: Long,
        date: LocalDate
    ): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transaction: Transaction): Completable

    @Query("SELECT * FROM transaction_list")
    fun getTransactionList(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transaction_list WHERE type = 0")
    fun getIncomeTransactionList(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transaction_list WHERE type = 1")
    fun getExpenseTransactionList(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transaction_list WHERE transactionId = :transactionId")
    fun getTransactionById(transactionId: Long): LiveData<Transaction>

    @Query("DELETE FROM transaction_list WHERE transactionId = :transactionId")
    fun removeTransaction(transactionId: Long): Completable

//    Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: Account): Completable

    @Query("SELECT * FROM account_list")
    fun getAccountList(): LiveData<List<Account>>

    @Query("SELECT * FROM account_list WHERE accountId = :accountId")
    fun getAccountById(accountId: Long): LiveData<Account>

    @Query("SELECT * FROM account_list")
    fun getAccountsWithTransactions(): LiveData<List<AccountWithTransactions>>

    @Query("DELETE FROM account_list WHERE accountId = :accountId")
    fun removeAccount(accountId: Long): Completable

    @Query("UPDATE account_list SET balance = :newBalance WHERE accountId = :accountId")
    fun updateAccountBalance(accountId: Long, newBalance: Double): Completable

    @Query("UPDATE account_list SET balance = balance - :amount WHERE accountId = :accountId")
    fun subtractAccountBalance(accountId: Long, amount: Double): Completable

    @Query("UPDATE account_list SET balance = balance + :amount WHERE accountId = :accountId")
    fun addAccountBalance(accountId: Long, amount: Double): Completable

    @Query("UPDATE account_list SET balance = 0")
    fun clearAllAccountBalances(): Completable

//    Sums

    @Query("SELECT SUM(amount) FROM transaction_list WHERE substr(date,6,2) = :month")
    fun getCashFlowByMonth(month: String): LiveData<Double>

    @Query("SELECT SUM(balance) FROM account_list")
    fun getOverallBalance(): LiveData<Double>

    @Query("SELECT SUM(amount) FROM transaction_list WHERE type = 0")
    fun getOverallIncome(): LiveData<Double>

    @Query("SELECT SUM(amount) FROM transaction_list WHERE type = 1")
    fun getOverallExpense(): LiveData<Double>
}