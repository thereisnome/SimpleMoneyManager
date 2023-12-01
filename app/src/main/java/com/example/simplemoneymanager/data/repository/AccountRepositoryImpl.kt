package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.account.AccountWithTransactions
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable

class AccountRepositoryImpl(private val moneyDao: MoneyDao): AccountRepository {
    override fun addAccount(account: Account): Completable {
        return moneyDao.addAccount(account)
    }

    override fun getAccountById(accountId: Long): LiveData<Account> {
        return moneyDao.getAccountById(accountId)
    }

    override fun getAccountList(): LiveData<List<Account>> {
        return moneyDao.getAccountList()
    }

    override fun getAccountWithTransactions(): LiveData<List<AccountWithTransactions>> {
        return moneyDao.getAccountsWithTransactions()
    }

    override fun removeAccount(accountId: Long): Completable {
        return moneyDao.removeAccount(accountId)
    }

    override fun updateAccountBalance(accountId: Long, newBalance: Double): Completable {
        return moneyDao.updateAccountBalance(accountId, newBalance)
    }

    override fun subtractAccountBalance(accountId: Long, amount: Double): Completable {
        return moneyDao.subtractAccountBalance(accountId, amount)
    }

    override fun addAccountBalance(accountId: Long, amount: Double): Completable {
        return moneyDao.addAccountBalance(accountId, amount)
    }

    override fun clearAllAccountBalances(): Completable {
        return moneyDao.clearAllAccountBalances()
    }

    override fun getOverallBalance(): LiveData<Double> {
        return moneyDao.getOverallBalance()
    }
}