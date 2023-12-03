package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAddBudgetBinding
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.presentation.viewModels.AddBudgetViewModel

private const val UNDEFINED_TRANSACTION = -1L

class AddBudgetFragment : Fragment(), CategoryBottomSheetDialogFragment.DataPassListener {

    private val viewModel: AddBudgetViewModel by viewModels()

    private val args by navArgs<AddBudgetFragmentArgs>()

    private lateinit var category: Category
    private lateinit var budget: Budget

    private var _binding: FragmentAddBudgetBinding? = null
    private val binding: FragmentAddBudgetBinding
        get() = _binding ?: throw RuntimeException("FragmentAddTransactionBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.budgetId != UNDEFINED_TRANSACTION) {
            launchEditMode()
        } else launchAddMode()

        setFocusOnAmountField()
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
                addBudget()
                true
            }
        }

        setBottomSheetClickListeners()
    }

    private fun launchEditMode() {
        binding.transactionAddToolbar.setTitle(R.string.edit_transaction)
        viewModel.getBudgetById(args.budgetId).observe(viewLifecycleOwner) {
            budget = it
            category = it.category
            binding.etAmount.setText(it.maxValue.toString())
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
        }

        with(binding) {
            transactionAddToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            transactionAddToolbar.inflateMenu(R.menu.add_transaction_toolbar_menu)
            binding.transactionAddToolbar.setOnMenuItemClickListener {
                editBudget()
                true
            }
        }
        setBottomSheetClickListeners()
    }

    private fun setBottomSheetClickListeners() {
        binding.buttonCategory.setOnClickListener {
            showCategoryBottomSheetDialog()
        }
    }

    private fun showCategoryBottomSheetDialog() {
        val bottomSheetDialogFragment =
            CategoryBottomSheetDialogFragment.newInstance(Category.EXPENSE)
        bottomSheetDialogFragment.setDataPassListener(this)
        bottomSheetDialogFragment.show(childFragmentManager, "TEST")
    }

    private fun addBudget() {
        if (checkInput()) {
            val maxValue = binding.etAmount.text.toString().toDouble()
            viewModel.addBudget(maxValue, category)
            findNavController().navigateUp()
        } else {
            binding.tilAmount.error = requireContext().getString(R.string.input_error)
        }
    }

    private fun editBudget() {
        if (checkInput()) {
            val maxValue = binding.etAmount.text.toString().toDouble()
            viewModel.editBudget(maxValue, args.budgetId, category)
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
}