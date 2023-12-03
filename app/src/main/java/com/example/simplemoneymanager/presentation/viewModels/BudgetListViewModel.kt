package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.BudgetRepositoryImpl
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactions
import com.example.simplemoneymanager.domain.budget.usecases.GetBudgetWithTransactionsUseCase
import com.example.simplemoneymanager.domain.budget.usecases.RemoveBudgetUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BudgetListViewModel(application: Application): AndroidViewModel(application) {
    private val db = MoneyDataBase.getInstance(application)
    private val budgetRepositoryImpl = BudgetRepositoryImpl(db.moneyDao())
    private val getBudgetWithTransactionsUseCase = GetBudgetWithTransactionsUseCase(budgetRepositoryImpl)
    private val removeBudgetUseCase = RemoveBudgetUseCase(budgetRepositoryImpl)

    private val compositeDisposable = CompositeDisposable()

    fun getBudgetWithTransactions(): LiveData<List<BudgetWithTransactions>> {
        return getBudgetWithTransactionsUseCase()
    }

    fun removeBudget(budgetId: Long){
        val disposable = removeBudgetUseCase(budgetId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Toast.makeText(getApplication(), "Budget removed", Toast.LENGTH_LONG).show()
        }, {
            Log.d("VM remove budget", it.message.toString())
        })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}