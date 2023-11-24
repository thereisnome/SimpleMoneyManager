package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAddTransactionBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.viewModels.AddTransactionViewModel

class AddTransactionFragment : Fragment(), CategoryBottomSheetDialogFragment.DataPassListener {

    private val viewModel by lazy {
        ViewModelProvider(this)[AddTransactionViewModel::class.java]
    }

    private var categoryType: Int = 0

    private var category: Category? = null

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

        binding.buttonIncome.setOnClickListener {
            categoryType = Category.INCOME
            binding.etCategory.setText("")
        }

        binding.buttonExpense.setOnClickListener {
            categoryType = Category.EXPENSE
            binding.etCategory.setText("")
        }

        binding.etCategory.setOnClickListener {
            showCategoryBottomSheetDialog()
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

    private fun addTransaction() {
        if (checkInput()) {
            val type = if (categoryType == Category.INCOME) {
                Transaction.INCOME
            } else Transaction.EXPENSE
            val name = binding.etName.text.toString()
            val amount = binding.etAmount.text.toString().toInt()
            viewModel.addTransaction(
                type, name, category?: Category.DEFAULT_CATEGORY, amount
            )
            findNavController().navigateUp()
        } else {
            binding.tilName.error = requireContext().getString(R.string.input_error)
            binding.tilAmount.error = requireContext().getString(R.string.input_error)
        }
    }

    private fun checkInput(): Boolean {
        return (binding.etName.text.toString() != "") && (binding.etAmount.text.toString() != "")
    }

    override fun onDataPassed(category: Category) {
        this.category = category
        binding.etCategory.setText(category.categoryName)
    }
}