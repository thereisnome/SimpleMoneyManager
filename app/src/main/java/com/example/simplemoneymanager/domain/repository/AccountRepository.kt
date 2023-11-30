package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.Account
import io.reactivex.rxjava3.core.Completable

interface AccountRepository {

    fun addAccount(account: Account): Completable

    fun getAccountById(accountId: Long): LiveData<Account>

    fun getAccountList(): LiveData<List<Account>>

    fun removeAccount(accountId: Long): Completable

    fun updateAccountBalance(accountId: Long, newBalance: Double): Completable

    fun subtractAccountBalance(accountId: Long, amount: Double): Completable

    fun addAccountBalance(accountId: Long, amount: Double): Completable

    fun clearAccountBalance(accountId: Long): Completable

    fun clearAllAccountBalances(): Completable

    fun getOverallBalance(): LiveData<Double>
}