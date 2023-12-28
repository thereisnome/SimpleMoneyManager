package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.common.ColorList
import com.example.simplemoneymanager.data.database.models.CategoryDbModel
import com.example.simplemoneymanager.databinding.FragmentAddCategoryDialogBinding
import com.example.simplemoneymanager.presentation.SimpleMoneyManagerApp
import com.example.simplemoneymanager.presentation.viewModels.AddCategoryDialogViewModel
import com.example.simplemoneymanager.presentation.viewModels.ViewModelFactory
import com.google.android.material.chip.Chip
import javax.inject.Inject

class AddCategoryDialogFragment private constructor(private val categoryType: Int) :
    DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<AddCategoryDialogViewModel>{
        viewModelFactory
    }

    private var _binding: FragmentAddCategoryDialogBinding? = null
    private val binding: FragmentAddCategoryDialogBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    private lateinit var color: String

    private val component by lazy { (requireActivity().application as SimpleMoneyManagerApp).component }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryDialogBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonId = when (categoryType) {
            CategoryDbModel.INCOME -> binding.buttonIncome.id
            else -> binding.buttonExpense.id
        }

        binding.toggleButtonCategoryType.check(buttonId)

        createChipGroup()

        with(binding) {
            categoryAddToolbar.setNavigationOnClickListener { dismiss() }
            categoryAddToolbar.inflateMenu(R.menu.add_category_toolbar_menu)
            categoryAddToolbar.setOnMenuItemClickListener {
                val categoryType =
                    if (toggleButtonCategoryType.checkedButtonId == buttonIncome.id){
                        CategoryDbModel.INCOME
                    } else CategoryDbModel.EXPENSE
                if (etName.text.toString() != ""){
                    val categoryName = etName.text.toString()
                    viewModel.addCategory(categoryType, categoryName, color)
                    Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_LONG)
                        .show()
                    dismiss()
                } else tilName.error = requireContext().getString(R.string.input_error)
                true
            }
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

    private fun createChipGroup() {
        val colorList = ColorList.entries
        colorList.forEach {color->
            val chip = Chip(requireContext())
            chip.chipStrokeWidth = 0f
            chip.chipBackgroundColor = ColorStateList.valueOf(color.hex.toColorInt())
            binding.chipGroupColor.addView(chip)
            chip.setOnClickListener {
                this.color = color.hex
            }
        }
    }

    companion object {
        fun newInstance(categoryType: Int): AddCategoryDialogFragment {
            return AddCategoryDialogFragment(categoryType)
        }
    }
}