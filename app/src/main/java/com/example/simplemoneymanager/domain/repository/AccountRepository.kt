package com.example.simplemoneymanager.domain.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.domain.account.Account
import io.reactivex.rxjava3.core.Completable

interface AccountRepository {

    fun addAccount(account: Account): Completable

    fun getAccountById(accountId: Int): LiveData<Account>

    fun getAccountList(): LiveData<List<Account>>

    fun removeAccount(accountId: Int): Completable

    fun updateAccountBalance(accountId: Int, newBalance: Int): Completable

    fun subtractAccountBalance(accountId: Int, amount: Int): Completable

    fun addAccountBalance(accountId: Int, amount: Int): Completable

    fun clearAccountBalance(accountId: Int): Completable

    fun clearAllAccountBalances(): Completable

    fun getOverallBalance(): LiveData<Int>
}