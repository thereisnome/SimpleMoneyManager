package com.example.simplemoneymanager.di

import androidx.lifecycle.ViewModel
import com.example.simplemoneymanager.presentation.viewModels.AccountBottomSheetViewModel
import com.example.simplemoneymanager.presentation.viewModels.AccountDetailsFragmentViewModel
import com.example.simplemoneymanager.presentation.viewModels.AccountListViewModel
import com.example.simplemoneymanager.presentation.viewModels.AddAccountDialogViewModel
import com.example.simplemoneymanager.presentation.viewModels.AddBudgetViewModel
import com.example.simplemoneymanager.presentation.viewModels.AddCategoryDialogViewModel
import com.example.simplemoneymanager.presentation.viewModels.AddTransactionViewModel
import com.example.simplemoneymanager.presentation.viewModels.BudgetListViewModel
import com.example.simplemoneymanager.presentation.viewModels.CategoryBottomSheetViewModel
import com.example.simplemoneymanager.presentation.viewModels.CategoryDetailsFragmentViewModel
import com.example.simplemoneymanager.presentation.viewModels.FilterBottomSheetViewModel
import com.example.simplemoneymanager.presentation.viewModels.HomeFragmentViewModel
import com.example.simplemoneymanager.presentation.viewModels.StatisticViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = AccountBottomSheetViewModel::class)
    fun bindAccountBottomSheetViewModel(viewModel: AccountBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AccountDetailsFragmentViewModel::class)
    fun bindAccountDetailsFragmentViewModel(viewModel: AccountDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AccountListViewModel::class)
    fun bindAccountListViewModel(viewModel: AccountListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AddAccountDialogViewModel::class)
    fun bindAddAccountDialogViewModel(viewModel: AddAccountDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AddBudgetViewModel::class)
    fun bindAddBudgetViewModel(viewModel: AddBudgetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AddCategoryDialogViewModel::class)
    fun bindAddCategoryDialogViewModel(viewModel: AddCategoryDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AddTransactionViewModel::class)
    fun bindAddTransactionViewModel(viewModel: AddTransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = BudgetListViewModel::class)
    fun bindBudgetListViewModel(viewModel: BudgetListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = CategoryBottomSheetViewModel::class)
    fun bindCategoryBottomSheetViewModel(viewModel: CategoryBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = CategoryDetailsFragmentViewModel::class)
    fun bindCategoryDetailsFragmentViewModel(viewModel: CategoryDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = FilterBottomSheetViewModel::class)
    fun bindFilterBottomSheetViewModel(viewModel: FilterBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = HomeFragmentViewModel::class)
    fun bindHomeFragmentViewModel(viewModel: HomeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = StatisticViewModel::class)
    fun bindStatisticViewModel(viewModel: StatisticViewModel): ViewModel
}