package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAddCategoryBottomSheetBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.presentation.viewModels.AddCategoryDialogViewModel

class AddCategoryDialogFragment private constructor(private val categoryType: Int) : DialogFragment() {

    private val viewModel: AddCategoryDialogViewModel by viewModels()

    private var _binding: FragmentAddCategoryBottomSheetBinding? = null
    private val binding: FragmentAddCategoryBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryBottomSheetBinding.inflate(
                inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(categoryType){
            Category.INCOME -> binding.toggleButtonCategoryType.check(binding.buttonIncome.id)
            Category.EXPENSE -> binding.toggleButtonCategoryType.check(binding.buttonExpense.id)
        }

        binding.toolbar.setNavigationOnClickListener { dismiss() }
        binding.toolbar.inflateMenu(R.menu.add_category_toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener {
            val categoryType = if(binding.toggleButtonCategoryType.checkedButtonId == binding.buttonIncome.id) Category.INCOME else Category.EXPENSE
            val categoryName = binding.etName.text.toString()
            viewModel.addCategory(categoryType, categoryName)
            dismiss()
            true
        }
    }

    override fun onStart() {
        super.onStart()
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance(categoryType: Int): AddCategoryDialogFragment{
            return AddCategoryDialogFragment(categoryType)
        }
    }
}