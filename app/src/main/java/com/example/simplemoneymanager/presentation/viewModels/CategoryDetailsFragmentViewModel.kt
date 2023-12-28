package com.example.simplemoneymanager.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.domain.account.usecases.SubtractAccountBalanceUseCase
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.usecases.GetCategoryByIdUseCase
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.domain.transaction.usecases.GetTransactionListUseCase
import com.example.simplemoneymanager.domain.transaction.usecases.RemoveTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CategoryDetailsFragmentViewModel @Inject constructor(
    private val removeTransactionUseCase: RemoveTransactionUseCase,
    private val subtractAccountBalanceUseCase: SubtractAccountBalanceUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getTransactionListUseCase: GetTransactionListUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getCategoryById(categoryId: Int): LiveData<CategoryEntity>{
        return getCategoryByIdUseCase(categoryId)
    }

    fun getTransactionList(): LiveData<List<TransactionEntity>>{
        return getTransactionListUseCase()
    }

    fun removeTransaction(transaction: TransactionEntity) {
        val disposable =
            removeTransactionUseCase.invoke(transaction.transactionId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d("CategoryDetailsFragmentViewModel", "Transaction removed $transaction")
                }, {
                    it.message?.let { it1 -> Log.d("CategoryDetailsFragmentViewModel", it1) }
                })
        compositeDisposable.add(disposable)
    }

    fun subtractAccountBalance(accountId: Long, amount: Double) {
        val disposable = subtractAccountBalanceUseCase(accountId, amount).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("CategoryDetailsFragmentViewModel", "ID: $accountId.accountId, amount: $amount")
            }, {
                Log.d("CategoryDetailsFragmentViewModel", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}