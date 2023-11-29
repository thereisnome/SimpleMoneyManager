package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAddTransactionBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.viewModels.AddTransactionViewModel
import kotlin.math.absoluteValue


private const val UNDEFINED_TRANSACTION = -1L

class AddTransactionFragment : Fragment(), CategoryBottomSheetDialogFragment.DataPassListener,
    AccountBottomSheetDialogFragment.DataPassListener {

    private val viewModel: AddTransactionViewModel by viewModels()

    private val args by navArgs<AddTransactionFragmentArgs>()

    private var categoryType: Int = 0

    private lateinit var category: Category
    private lateinit var account: Account
    private lateinit var transaction: Transaction

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

        if (args.transactionId != UNDEFINED_TRANSACTION) {
            launchEditMode()
            setOnBackPressed()

        } else launchAddMode()

        setFocusOnAmountField()
    }

    private fun setFocusOnAmountField() {
        binding.etAmount.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etAmount, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun launchAddMode() {

        binding.tilAmount.prefixText = "+"

        viewModel.getMainAccount().observe(viewLifecycleOwner) {
            account = it
        }

        viewModel.getDefaultCategory().observe(viewLifecycleOwner) {
            category = it
        }

        with(binding) {
            transactionAddToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            transactionAddToolbar.inflateMenu(R.menu.add_transaction_toolbar_menu)
            binding.transactionAddToolbar.setOnMenuItemClickListener {
                addTransaction()
                true
            }
        }

        setCategoryButtonsClickListeners()
        setBottomSheetClickListeners()
    }

    private fun launchEditMode() {
        binding.transactionAddToolbar.setTitle(R.string.edit_transaction)
        viewModel.getTransactionById(args.transactionId).observe(viewLifecycleOwner) {
            transaction = it
            category = it.category
            categoryType = it.type
            account = it.account

            if(transaction.type==Transaction.INCOME){
                binding.tilAmount.prefixText = "-"
            } else binding.tilAmount.prefixText = "+"

            viewModel.subtractAccountBalance(account, transaction.amount)

            binding.etAmount.setText(it.amount.absoluteValue.toString())
            binding.etName.setText(it.transactionName)
            binding.etCategory.setText(it.category.categoryName)
            binding.etAccount.setText(it.account.accountName)

            val checkedButtonId =
                if (transaction.type == Transaction.INCOME) binding.buttonIncome.id else binding.buttonExpense.id
            binding.toggleButtonTransactionType.check(checkedButtonId)
        }

        with(binding) {
            transactionAddToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
                viewModel.addAccountBalance(account, transaction.amount)
            }
            transactionAddToolbar.inflateMenu(R.menu.add_transaction_toolbar_menu)
            binding.transactionAddToolbar.setOnMenuItemClickListener {
                editTransaction()
                true
            }
        }
        setCategoryButtonsClickListeners()
        setBottomSheetClickListeners()
    }

    private fun setBottomSheetClickListeners() {
        binding.buttonCategory.setOnClickListener {
            showCategoryBottomSheetDialog()
        }

        binding.etAccount.setOnClickListener {
            showAccountBottomSheetDialog()
        }
    }

    private fun setCategoryButtonsClickListeners() {
        binding.buttonIncome.setOnClickListener {
            categoryType = Category.INCOME
            binding.tilAmount.prefixText = "+"
            binding.buttonCategory.text = requireContext().getString(R.string.no_category)
        }

        binding.buttonExpense.setOnClickListener {
            categoryType = Category.EXPENSE
            binding.tilAmount.prefixText = "-"
            binding.buttonCategory.text = requireContext().getString(R.string.no_category)
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

    private fun editTransaction() {
        if (checkInput()) {
            val type = if (categoryType == Category.INCOME) {
                Transaction.INCOME
            } else Transaction.EXPENSE
            val name = binding.etName.text.toString()
            val amount = if (type == Transaction.INCOME) {
                binding.etAmount.text.toString().toInt()
            } else -binding.etAmount.text.toString().toInt()
            viewModel.editTransaction(args.transactionId, type, name, category, amount, account)
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
        binding.buttonCategory.text = category.categoryName
    }

    override fun onAccountPassed(account: Account) {
        this.account = account
        binding.etAccount.setText(account.accountName)
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.addAccountBalance(account, transaction.amount)
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}