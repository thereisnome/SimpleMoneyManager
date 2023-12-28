package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.usecases.AddAccountBalanceUseCase
import com.example.simplemoneymanager.domain.account.usecases.GetMainAccountUseCase
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.usecases.GetDefaultCategoryUseCase
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.domain.transaction.usecases.AddTransactionUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.EditTransactionUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionByIdUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate
import javax.inject.Inject

class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val addAccountBalanceUseCase: AddAccountBalanceUseCase,
    private val getDefaultCategoryUseCase: GetDefaultCategoryUseCase,
    private val getMainAccountUseCase: GetMainAccountUseCase,
    private val editTransactionUseCase: EditTransactionUseCase,
    private val subtractAccountBalanceUseCase: SubtractAccountBalanceUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun addTransaction(
        type: Int,
        name: String,
        category: CategoryEntity,
        amount: Double,
        account: AccountEntity,
        date: LocalDate
    ) {
        val transaction = TransactionEntity(type, name, category, amount, account, date.toString())
        val disposable = addTransactionUseCase(transaction).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            }, {
                it.message?.let { it1 -> Log.d("VM Add Transaction", it1) }
            })
        compositeDisposable.add(disposable)
    }

    fun getDefaultCategory(): LiveData<CategoryEntity> {
        return getDefaultCategoryUseCase()
    }

    fun getMainAccount(): LiveData<AccountEntity> {
        return getMainAccountUseCase()
    }

    fun addAccountBalance(account: AccountEntity, amount: Double) {
        val disposable = addAccountBalanceUseCase(account, amount).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("VM updateAccountBalance", "ID: $account.accountId, amount: $amount")
            }, {
                Log.d("VM updateAccountBalance", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    fun subtractAccountBalance(account: AccountEntity, amount: Double) {
        val disposable =
            subtractAccountBalanceUseCase(account.accountId, amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d("VM updateAccountBalance", "ID: $account.accountId, amount: $amount")
                }, {
                    Log.d("VM updateAccountBalance", it.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    fun getTransactionById(transactionId: Long): LiveData<TransactionEntity> {
        return getTransactionByIdUseCase(transactionId)
    }

    fun editTransaction(
        transactionId: Long,
        type: Int,
        name: String,
        category: CategoryEntity,
        amount: Double,
        account: AccountEntity,
        date: LocalDate
    ) {
        val transaction =
            TransactionEntity(type, name, category, amount, account, date.toString(), transactionId)
        val disposable = editTransactionUseCase(transaction).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("VM edit transaction", "Transaction edited: ${transaction.transactionId}")
            }, {
                Log.d("VM edit transaction", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}