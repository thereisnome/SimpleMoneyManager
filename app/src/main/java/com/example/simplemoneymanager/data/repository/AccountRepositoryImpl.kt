package com.example.simplemoneymanager.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.data.mappers.Mapper
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.AccountWithTransactionsEntity
import com.example.simplemoneymanager.domain.repository.AccountRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val moneyDao: MoneyDao,
    private val mapper: Mapper
) : AccountRepository {
    override fun addAccount(account: AccountEntity): Completable {
        return moneyDao.addAccount(mapper.mapAccountEntityToDbModel(account))
    }

    override fun getAccountById(accountId: Long): LiveData<AccountEntity> {
        return moneyDao.getAccountById(accountId).map { accountDbModel ->
            mapper.mapAccountDbModelToEntity(accountDbModel)
        }
    }

    override fun getAccountList(): LiveData<List<AccountEntity>> {
        return moneyDao.getAccountList().map { accountDbModelList ->
            accountDbModelList.map { mapper.mapAccountDbModelToEntity(it) }
        }
    }

    override fun getAccountWithTransactions(): LiveData<List<AccountWithTransactionsEntity>> {
        return moneyDao.getAccountsWithTransactions().map { accountWithTransactionsDbModelList ->
            accountWithTransactionsDbModelList.map {
                mapper.mapAccountWithTransactionsDbModelToEntity(
                    it
                )
            }
        }
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