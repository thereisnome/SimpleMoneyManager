package com.example.simplemoneymanager.di

import android.app.Application
import com.example.simplemoneymanager.data.database.MoneyDao
import com.example.simplemoneymanager.data.database.MoneyDataBase
import com.example.simplemoneymanager.data.repository.AccountRepositoryImpl
import com.example.simplemoneymanager.data.repository.BudgetRepositoryImpl
import com.example.simplemoneymanager.data.repository.CategoryRepositoryImpl
import com.example.simplemoneymanager.data.repository.TransactionRepositoryImpl
import com.example.simplemoneymanager.domain.repository.AccountRepository
import com.example.simplemoneymanager.domain.repository.BudgetRepository
import com.example.simplemoneymanager.domain.repository.CategoryRepository
import com.example.simplemoneymanager.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

    @Binds
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    companion object{

        @Provides
        fun provideMoneyDao(application: Application): MoneyDao{
            return MoneyDataBase.getInstance(application).moneyDao()
        }
    }
}