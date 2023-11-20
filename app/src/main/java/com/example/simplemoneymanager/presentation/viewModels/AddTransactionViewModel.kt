package com.example.simplemoneymanager.presentation.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.domain.transaction.usecases.AddTransactionUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate

class AddTransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MoneyDataBase.getInstance(application)
    private val transactionRepositoryImpl = TransactionRepositoryImpl(db.moneyDao())
    private val addTransactionUseCase = AddTransactionUseCase(transactionRepositoryImpl)
    private val compositeDisposable = CompositeDisposable()

    private val _finishActivity = MutableLiveData<Unit>()
    val finishActivity: LiveData<Unit>
        get() = _finishActivity

    fun addTransaction(type: Int, name: String, category: Category, amount: Int) {
        val transaction = Transaction(type, name, category, amount, LocalDate.now())
        val disposable = addTransactionUseCase.invoke(transaction).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                closeActivity()
            }, {
                it.message?.let { it1 -> Log.d("VM Add Transaction", it1) }
            })
        compositeDisposable.add(disposable)
    }

    private fun closeActivity() {
        _finishActivity.value = Unit
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}