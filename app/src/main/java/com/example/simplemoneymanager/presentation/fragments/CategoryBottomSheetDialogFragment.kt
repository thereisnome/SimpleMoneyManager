package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentCategoryBottomSheetBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.presentation.recyclerViews.CategoryListAdapter
import com.example.simplemoneymanager.presentation.viewModels.CategoryBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottomSheetDialogFragment private constructor(private val categoryType: Int) :
    BottomSheetDialogFragment() {

    private var dataPassListener: DataPassListener? = null

    private val adapter = CategoryListAdapter()

    private val categoryBottomSheetViewModel by lazy {
        ViewModelProvider(this)[CategoryBottomSheetViewModel::class.java]
    }

    private var _binding: FragmentCategoryBottomSheetBinding? = null
    private val binding: FragmentCategoryBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBottomSheetBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (categoryType == Category.EXPENSE) {
            categoryBottomSheetViewModel.getExpenseCategoryList()
                .observe(viewLifecycleOwner) { categoryList ->
                    adapter.submitList(categoryList)
                    binding.rvCategories.adapter = adapter
                    adapter.onItemClickListener = {
                        passDataBack(it)
                        dismiss()
                    }
                }
        } else {
            categoryBottomSheetViewModel.getIncomeCategoryList()
                .observe(viewLifecycleOwner) { categoryList ->
                    adapter.submitList(categoryList)
                    binding.rvCategories.adapter = adapter
                    adapter.onItemClickListener = {
                        passDataBack(it)
                        dismiss()
                    }
                }
        }
    }

    fun setDataPassListener(listener: DataPassListener) {
        dataPassListener = listener
    }

    private fun passDataBack(category: Category) {
        dataPassListener?.onDataPassed(category)
    }

    interface DataPassListener {
        fun onDataPassed(category: Category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(categoryType: Int): CategoryBottomSheetDialogFragment {
            return CategoryBottomSheetDialogFragment(categoryType)
        }
    }
}