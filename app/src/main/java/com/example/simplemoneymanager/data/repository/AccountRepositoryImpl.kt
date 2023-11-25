package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable

class AccountRepositoryImpl(private val moneyDao: MoneyDao): AccountRepository {
    override fun addAccount(account: Account): Completable {
        return moneyDao.addAccount(account)
    }

    override fun getAccountById(accountId: Int): LiveData<Account> {
        return moneyDao.getAccountById(accountId)
    }

    override fun getAccountList(): LiveData<List<Account>> {
        return moneyDao.getAccountList()
    }

    override fun removeAccount(accountId: Int): Completable {
        return moneyDao.removeAccount(accountId)
    }

    override fun updateAccountBalance(accountId: Int, newBalance: Int): Completable {
        return moneyDao.updateAccountBalance(accountId, newBalance)
    }

    override fun subtractAccountBalance(accountId: Int, amount: Int): Completable {
        return moneyDao.subtractAccountBalance(accountId, amount)
    }

    override fun addAccountBalance(accountId: Int, amount: Int): Completable {
        return moneyDao.addAccountBalance(accountId, amount)
    }

    override fun clearAccountBalance(accountId: Int): Completable {
        return moneyDao.clearAccountBalance(accountId)
    }

    override fun clearAllAccountBalances(): Completable {
        return moneyDao.clearAllAccountBalances()
    }
}