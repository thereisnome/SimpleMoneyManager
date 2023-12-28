package com.example.simplemoneymanager.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simplemoneymanager.data.database.models.AccountDbModel
import com.example.simplemoneymanager.data.database.models.AccountWithTransactionsDbModel
import com.example.simplemoneymanager.data.database.models.BudgetDbModel
import com.example.simplemoneymanager.data.database.models.BudgetWithTransactionsDbModel
import com.example.simplemoneymanager.data.database.models.CategoryDbModel
import com.example.simplemoneymanager.data.database.models.CategoryWithTransactionsDbModel
import com.example.simplemoneymanager.data.database.models.TransactionDbModel
import io.reactivex.rxjava3.core.Completable
import java.time.LocalDate

@Dao
interface MoneyDao {

//    Category

    @Query("DELETE FROM category_list WHERE id = :categoryId")
    fun removeCategory(categoryId: Int): Completable

    @Query("SELECT * FROM category_list WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): LiveData<CategoryDbModel>

    @Query("SELECT * FROM category_list WHERE categoryType = 1")
    fun getExpenseCategoryList(): LiveData<List<CategoryDbModel>>

    @Query("SELECT * FROM category_list WHERE categoryType = 0")
    fun getIncomeCategoryList(): LiveData<List<CategoryDbModel>>

    @Query("SELECT * FROM category_list")
    fun getCategoryList(): LiveData<List<CategoryDbModel>>

    @Query("SELECT * FROM category_list")
    fun getCategoryWithTransactions(): LiveData<List<CategoryWithTransactionsDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: CategoryDbModel): Completable

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
    fun addTransaction(transaction: TransactionDbModel): Completable

    @Query("SELECT * FROM transaction_list")
    fun getTransactionList(): LiveData<List<TransactionDbModel>>

    @Query("SELECT * FROM transaction_list WHERE type = 0")
    fun getIncomeTransactionList(): LiveData<List<TransactionDbModel>>

    @Query("SELECT * FROM transaction_list WHERE type = 1")
    fun getExpenseTransactionList(): LiveData<List<TransactionDbModel>>

    @Query("SELECT * FROM transaction_list WHERE transactionId = :transactionId")
    fun getTransactionById(transactionId: Long): LiveData<TransactionDbModel>

    @Query("DELETE FROM transaction_list WHERE transactionId = :transactionId")
    fun removeTransaction(transactionId: Long): Completable

//    Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: AccountDbModel): Completable

    @Query("SELECT * FROM account_list")
    fun getAccountList(): LiveData<List<AccountDbModel>>

    @Query("SELECT * FROM account_list WHERE accountId = :accountId")
    fun getAccountById(accountId: Long): LiveData<AccountDbModel>

    @Query("SELECT * FROM account_list")
    fun getAccountsWithTransactions(): LiveData<List<AccountWithTransactionsDbModel>>

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

//    Budget

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBudget(budget: BudgetDbModel): Completable

    @Query("UPDATE budget_list SET maxValue = :maxValue, id = :categoryId WHERE budgetId = :budgetId")
    fun editBudgetById(budgetId: Long, maxValue: Double, categoryId: Int): Completable

    @Query("SELECT * FROM budget_list")
    fun getBudgetList(): LiveData<List<BudgetDbModel>>

    @Query("SELECT * FROM budget_list WHERE budgetId = :budgetId")
    fun getBudgetById(budgetId: Long): LiveData<BudgetDbModel>

    @Query("DELETE FROM budget_list WHERE budgetId = :budgetId")
    fun removeBudget(budgetId: Long): Completable

    @Query("SELECT * FROM budget_list")
    fun getBudgetWithTransactions(): LiveData<List<BudgetWithTransactionsDbModel>>

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