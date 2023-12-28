package com.example.simplemoneymanager.di

import android.app.Application
import com.example.simplemoneymanager.presentation.fragments.AccountBottomSheetDialogFragment
import com.example.simplemoneymanager.presentation.fragments.AccountDetailsFragment
import com.example.simplemoneymanager.presentation.fragments.AccountListFragment
import com.example.simplemoneymanager.presentation.fragments.AddAccountDialogFragment
import com.example.simplemoneymanager.presentation.fragments.AddBudgetFragment
import com.example.simplemoneymanager.presentation.fragments.AddCategoryDialogFragment
import com.example.simplemoneymanager.presentation.fragments.AddTransactionFragment
import com.example.simplemoneymanager.presentation.fragments.BudgetListFragment
import com.example.simplemoneymanager.presentation.fragments.CategoryBottomSheetDialogFragment
import com.example.simplemoneymanager.presentation.fragments.CategoryDetailsFragment
import com.example.simplemoneymanager.presentation.fragments.FilterBottomSheetDialogFragment
import com.example.simplemoneymanager.presentation.fragments.HomeFragment
import com.example.simplemoneymanager.presentation.fragments.StatisticFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [
    ViewModelModule::class,
    DataModule::class
])
interface AppComponent {

    fun inject(fragment: AccountBottomSheetDialogFragment)
    fun inject(fragment: AccountDetailsFragment)
    fun inject(fragment: AccountListFragment)
    fun inject(fragment: AddAccountDialogFragment)
    fun inject(fragment: AddBudgetFragment)
    fun inject(fragment: AddCategoryDialogFragment)
    fun inject(fragment: AddTransactionFragment)
    fun inject(fragment: BudgetListFragment)
    fun inject(fragment: CategoryBottomSheetDialogFragment)
    fun inject(fragment: CategoryDetailsFragment)
    fun inject(fragment: FilterBottomSheetDialogFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: StatisticFragment)

    @Component.Factory
    interface Factory{

        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}