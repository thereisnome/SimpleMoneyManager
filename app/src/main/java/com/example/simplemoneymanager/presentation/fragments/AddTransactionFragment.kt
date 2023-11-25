package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAddTransactionBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.viewModels.AddTransactionViewModel

class AddTransactionFragment : Fragment(), CategoryBottomSheetDialogFragment.DataPassListener,
    AccountBottomSheetDialogFragment.DataPassListener {

    private val viewModel: AddTransactionViewModel by viewModels()

    private var categoryType: Int = 0

    private lateinit var category: Category
    private lateinit var account: Account
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding: FragmentAddTransactionBinding
        get() = _binding ?: throw RuntimeException("FragmentAddTransactionBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMainAccount().observe(viewLifecycleOwner) {
            account = it
        }

        viewModel.getDefaultCategory().observe(viewLifecycleOwner) {
            category = it
        }

        binding.buttonIncome.setOnClickListener {
            categoryType = Category.INCOME
            if (binding.etCategory.text.toString() != "No category") {
                binding.etCategory.setText("")
            }
        }

        binding.buttonExpense.setOnClickListener {
            categoryType = Category.EXPENSE
            if (binding.etCategory.text.toString() != "No category") {
                binding.etCategory.setText("")
            }
        }

        binding.etCategory.setOnClickListener {
            showCategoryBottomSheetDialog()
        }

        binding.etAccount.setOnClickListener {
            showAccountBottomSheetDialog()
        }

        with(binding) {
            transactionAddToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            transactionAddToolbar.inflateMenu(R.menu.add_transaction_toolbar_menu)
            transactionAddToolbar.setOnMenuItemClickListener {
                addTransaction()
                true
            }
        }
    }

    private fun showCategoryBottomSheetDialog() {
        val bottomSheetDialogFragment = CategoryBottomSheetDialogFragment.newInstance(categoryType)
        bottomSheetDialogFragment.setDataPassListener(this)
        bottomSheetDialogFragment.show(childFragmentManager, "TEST")
    }

    private fun showAccountBottomSheetDialog() {
        val bottomSheetDialogFragment = AccountBottomSheetDialogFragment()
        bottomSheetDialogFragment.setDataPassListener(this)
        bottomSheetDialogFragment.show(childFragmentManager, "TEST")
    }

    private fun addTransaction() {
        if (checkInput()) {
            val type = if (categoryType == Category.INCOME) {
                Transaction.INCOME
            } else Transaction.EXPENSE
            val name = binding.etName.text.toString()
            val amount = if (type == Transaction.INCOME) {
                binding.etAmount.text.toString().toInt()
            } else -binding.etAmount.text.toString().toInt()
            viewModel.addTransaction(type, name, category, amount, account)
            viewModel.addAccountBalance(account, amount)
            findNavController().navigateUp()
        } else {
            binding.tilAmount.error = requireContext().getString(R.string.input_error)
        }
    }

    private fun checkInput(): Boolean {
        return binding.etAmount.text.toString() != ""
    }

    override fun onCategoryPassed(category: Category) {
        this.category = category
        binding.etCategory.setText(category.categoryName)
    }

    override fun onAccountPassed(account: Account) {
        this.account = account
        binding.etAccount.setText(account.accountName)
    }
}