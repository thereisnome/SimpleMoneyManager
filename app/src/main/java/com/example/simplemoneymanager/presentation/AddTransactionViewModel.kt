package com.example.simplemoneymanager.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplemoneymanager.data.MoneyDataBase
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.day.Day
import com.example.simplemoneymanager.domain.transaction.AddTransactionUseCase
import com.example.simplemoneymanager.domain.transaction.Transaction
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddTransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val addTransactionUseCase = AddTransactionUseCase(db.moneyDao())
    private val compositeDisposable = CompositeDisposable()

    private val _finishActivity = MutableLiveData<Unit>()
    val finishActivity: LiveData<Unit>
        get() = _finishActivity

    fun addTransaction(type: Int, name: String, category: Category, amount: Int, account: Account){
        var transaction = Transaction(type, name, category, amount, account)
        val disposable = addTransactionUseCase.addTransaction(transaction).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            closeActivity()
        }, {
            it.message?.let { it1 -> Log.d("VM Add Transaction", it1) }
        })
    }

    private fun closeActivity(){
        _finishActivity.value = Unit
    }


}