package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.common.ColorList
import com.example.simplemoneymanager.databinding.FragmentAddTransactionBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.viewModels.AddTransactionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
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
    private var date = LocalDate.now()

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
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvAmount.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFocusOnAmountField() {
        binding.etAmount.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etAmount, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun launchAddMode() {
        binding.tvAmount.prefixText = "+"
        binding.buttonDate.text = LocalDate.now().toString()
        viewModel.getMainAccount().observe(viewLifecycleOwner) {
            account = it
            binding.buttonAccount.setBackgroundColor(it.accountColor.toColorInt())
            binding.buttonAccount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        }

        viewModel.getDefaultCategory().observe(viewLifecycleOwner) {
            category = it
            binding.buttonCategory.setBackgroundColor(it.categoryColor.toColorInt())
            binding.buttonCategory.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
        }

        with(binding) {
            transactionAddToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            transactionAddToolbar.inflateMenu(R.menu.add_transaction_toolbar_menu)
            binding.transactionAddToolbar.setOnMenuItemClickListener {
                addTransaction()
                true
            }
        }

        setDateButtonClickListener()
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
            date = it.date
            if (transaction.type == Transaction.INCOME) {
                binding.tvAmount.prefixText = "-"
            } else binding.tvAmount.prefixText = "+"
            viewModel.subtractAccountBalance(account, transaction.amount)
            binding.etAmount.setText(it.amount.absoluteValue.toString())
            binding.etName.setText(it.transactionName)
            binding.buttonCategory.text = it.category.categoryName
            binding.buttonCategory.setBackgroundColor((it.category.categoryColor).toColorInt())
            val categoryContrast = ColorUtils.calculateContrast(
                binding.buttonCategory.currentTextColor,
                category.categoryColor.toColorInt()
            )
            if (categoryContrast < 1.5f) {
                binding.buttonCategory.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            } else binding.buttonCategory.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            val accountContrast = ColorUtils.calculateContrast(
                binding.buttonAccount.currentTextColor,
                account.accountColor.toColorInt()
            )
            if (accountContrast < 1.5f) {
                binding.buttonAccount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            } else binding.buttonAccount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.buttonAccount.text = it.account.accountName
            binding.buttonAccount.setBackgroundColor(it.account.accountColor.toColorInt())
            binding.buttonDate.text = it.date.toString()
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

        setDateButtonClickListener()
        setCategoryButtonsClickListeners()
        setBottomSheetClickListeners()
    }

    private fun setBottomSheetClickListeners() {
        binding.buttonCategory.setOnClickListener {
            showCategoryBottomSheetDialog()
        }

        binding.buttonAccount.setOnClickListener {
            showAccountBottomSheetDialog()
        }
    }

    private fun setCategoryButtonsClickListeners() {
        binding.buttonIncome.setOnClickListener {
            categoryType = Category.INCOME
            binding.tvAmount.prefixText = "+"
            binding.tvAmount.error = null
            resetCategoryButton()
        }

        binding.buttonExpense.setOnClickListener {
            categoryType = Category.EXPENSE
            binding.tvAmount.prefixText = "-"
            binding.tvAmount.error = null
            resetCategoryButton()
        }
    }

    private fun setDateButtonClickListener() {
        val datePicker = buildDatePicker()

        binding.buttonDate.setOnClickListener {
            datePicker.show(childFragmentManager, "TAG")
        }

        datePicker.addOnPositiveButtonClickListener {
            date = datePicker.selection?.let { it1 -> formatDateToLocalDate(it1) }
            binding.buttonDate.text = formatDateToString(it)
        }
    }

    private fun formatDateToLocalDate(date: Long): LocalDate {
        val formatToDigits = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        return LocalDate.parse(formatToDigits.format(date))
    }

    private fun formatDateToString(date: Long): String {
        val formatToString = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        return formatToString.format(date)
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText(requireContext().getString(R.string.select_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
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
        when (checkInput()) {
            0 -> {
                val type = if (categoryType == Category.INCOME) {
                    Transaction.INCOME
                } else Transaction.EXPENSE
                val name = binding.etName.text.toString()
                val amount = if (type == Transaction.INCOME) {
                    binding.etAmount.text.toString().toDouble()
                } else -binding.etAmount.text.toString().toDouble()
                viewModel.addTransaction(type, name, category, amount, account, date)
                viewModel.addAccountBalance(account, amount)
                findNavController().navigateUp()
            }

            1 -> binding.tvAmount.error = requireContext().getString(R.string.input_error)
            2 -> binding.tvAmount.error = requireContext().getString(R.string.balance_error)
        }
    }

    private fun editTransaction() {
        when (checkInput()) {
            0 -> {
                val type = if (categoryType == Category.INCOME) {
                    Transaction.INCOME
                } else Transaction.EXPENSE
                val name = binding.etName.text.toString()
                val amount = if (type == Transaction.INCOME) {
                    binding.etAmount.text.toString().toDouble()
                } else -binding.etAmount.text.toString().toDouble()
                viewModel.editTransaction(
                    args.transactionId,
                    type,
                    name,
                    category,
                    amount,
                    account,
                    date
                )
                viewModel.addAccountBalance(account, amount)
                findNavController().navigateUp()
            }

            1 -> binding.tvAmount.error = requireContext().getString(R.string.input_error)
            2 -> binding.tvAmount.error = requireContext().getString(R.string.balance_error)
        }


    }

    private fun checkInput(): Int {
        val type =
            if (binding.toggleButtonTransactionType.checkedButtonId == binding.buttonIncome.id) 0 else 1
        return if (binding.etAmount.text.toString().isEmpty()) {
            1
        } else if (type == Transaction.EXPENSE && account.balance - binding.etAmount.text.toString()
                .toDouble() < 0
        ) {
            2
        } else 0
    }

    private fun resetCategoryButton() {
        binding.buttonCategory.text = requireContext().getString(R.string.no_category)
        binding.buttonCategory.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.buttonCategory.setBackgroundColor(ColorList.BLUE_CHALK.hex.toColorInt())
    }

    override fun onCategoryPassed(category: Category) {
        this.category = category

        val contrast = ColorUtils.calculateContrast(
            binding.buttonCategory.currentHintTextColor,
            category.categoryColor.toColorInt()
        )

        if (contrast < 1.5f) {
            binding.buttonCategory.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
        } else binding.buttonCategory.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        binding.buttonCategory.text = category.categoryName
        binding.buttonCategory.setBackgroundColor((category.categoryColor).toColorInt())
    }

    override fun onAccountPassed(account: Account) {
        this.account = account
        val contrast = ColorUtils.calculateContrast(
            binding.buttonAccount.currentHintTextColor,
            account.accountColor.toColorInt()
        )

        if (contrast < 1.5f) {
            binding.buttonAccount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
        } else binding.buttonAccount.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        binding.buttonAccount.text = account.accountName
        binding.buttonAccount.setBackgroundColor((account.accountColor).toColorInt())
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