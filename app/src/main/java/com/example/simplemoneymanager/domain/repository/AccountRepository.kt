package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.AccountWithTransactionsEntity
import io.reactivex.rxjava3.core.Completable

interface AccountRepository {

    fun addAccount(account: AccountEntity): Completable

    fun getAccountById(accountId: Long): LiveData<AccountEntity>

    fun getAccountList(): LiveData<List<AccountEntity>>

    fun getAccountWithTransactions(): LiveData<List<AccountWithTransactionsEntity>>

    fun removeAccount(accountId: Long): Completable

    fun updateAccountBalance(accountId: Long, newBalance: Double): Completable

    fun subtractAccountBalance(accountId: Long, amount: Double): Completable

    fun addAccountBalance(accountId: Long, amount: Double): Completable

    fun clearAllAccountBalances(): Completable

    fun getOverallBalance(): LiveData<Double>
}