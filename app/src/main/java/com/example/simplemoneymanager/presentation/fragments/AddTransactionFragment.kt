package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simplemoneymanager.databinding.FragmentAddTransactionBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.viewModels.AddTransactionViewModel

class AddTransactionFragment : Fragment(), CategoryBottomSheetDialogFragment.DataPassListener {
    private lateinit var onEditingFinishListener: OnEditingFinishListener

    private val addTransactionViewModel by lazy {
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishListener) {
            onEditingFinishListener = context
        } else throw RuntimeException("Activity must implement OnEditingFinishListener")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toggleButtonTransactionType.check(binding.buttonIncome.id)

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

        binding.buttonSave.setOnClickListener {
            addTransaction()
        }

        addTransactionViewModel.finishActivity.observe(viewLifecycleOwner) {
            onEditingFinishListener.onEditingFinished()
        }
    }

    private fun showCategoryBottomSheetDialog(){
        val bottomSheetDialogFragment = CategoryBottomSheetDialogFragment.newInstance(categoryType)
        bottomSheetDialogFragment.setDataPassListener(this)
        bottomSheetDialogFragment.show(childFragmentManager, "TEST")
    }

    private fun addTransaction() {
        val type = if (categoryType == Category.INCOME) {
            Transaction.INCOME
        } else Transaction.EXPENSE
        val name = binding.etName.text.toString()
        val amount = binding.etAmount.text.toString().toInt()
        addTransactionViewModel.addTransaction(type, name, category?: Category(0, "No category", -1), amount)
    }

    interface OnEditingFinishListener {
        fun onEditingFinished()
    }

    override fun onDataPassed(category: Category) {
        this.category = category
        binding.etCategory.setText(category.name)
    }
}