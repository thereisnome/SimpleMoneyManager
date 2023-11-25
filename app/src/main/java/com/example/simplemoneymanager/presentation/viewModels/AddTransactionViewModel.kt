package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.account.usecases.AddAccountBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.GetMainAccountUseCase
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.usecases.GetDefaultCategoryUseCase
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.domain.transaction.usecases.AddTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate

class AddTransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val transactionRepositoryImpl = TransactionRepositoryImpl(db.moneyDao())
    private val accountRepositoryImpl = AccountRepositoryImpl(db.moneyDao())
    private val categoryRepositoryImpl = CategoryRepositoryImpl(db.moneyDao())
    private val addTransactionUseCase = AddTransactionUseCase(transactionRepositoryImpl)
    private val addAccountBalanceUseCase = AddAccountBalanceUseCase(accountRepositoryImpl)
    private val getDefaultCategoryUseCase = GetDefaultCategoryUseCase(categoryRepositoryImpl)
    private val getMainAccountUseCase = GetMainAccountUseCase(accountRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun addTransaction(type: Int, name: String, category: Category, amount: Int, account: Account) {
        val transaction = Transaction(type, name, category, amount, account, LocalDate.now())
        val disposable = addTransactionUseCase.invoke(transaction).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            }, {
                it.message?.let { it1 -> Log.d("VM Add Transaction", it1) }
            })
        compositeDisposable.add(disposable)
    }

    fun getDefaultCategory(): LiveData<Category> {
        return getDefaultCategoryUseCase()
    }

    fun getMainAccount(): LiveData<Account> {
        return getMainAccountUseCase()
    }

    fun addAccountBalance(account: Account, amount: Int) {
        val disposable = addAccountBalanceUseCase(account, amount).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("VM updateAccountBalance", "ID: $account.accountId, amount: $amount")
        }, {
            Log.d("VM updateAccountBalance", it.message.toString())
        })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}