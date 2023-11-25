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
    BottomSheetDialogFragment(), CategoryListAdapter.CategoryPopupMenuItemClickListener {

    private var dataPassListener: DataPassListener? = null

    private val adapter = CategoryListAdapter(this)

    private val viewModel by lazy {
        ViewModelProvider(this)[CategoryBottomSheetViewModel::class.java]
    }

    private var _binding: FragmentCategoryBottomSheetBinding? = null
    private val binding: FragmentCategoryBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

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

        binding.buttonAddNewCategory.setOnClickListener {
            showAddCategoryBottomSheet()
        }

        if (categoryType == Category.EXPENSE) {
            viewModel.getExpenseCategoryList()
                .observe(viewLifecycleOwner) { categoryList ->
                    adapter.submitList(categoryList)
                    binding.rvCategories.adapter = adapter
                    adapter.onItemClickListener = {
                        passDataBack(it)
                        dismiss()
                    }
                }
        } else {
            viewModel.getIncomeCategoryList()
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

    private fun showAddCategoryBottomSheet() {
        val addCategoryDialogFragment = AddCategoryDialogFragment.newInstance(categoryType)
        addCategoryDialogFragment.show(childFragmentManager, "TEST")
    }

    fun setDataPassListener(listener: DataPassListener) {
        dataPassListener = listener
    }

    private fun passDataBack(category: Category) {
        dataPassListener?.onCategoryPassed(category)
    }

    interface DataPassListener {
        fun onCategoryPassed(category: Category)
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

    override fun onMenuItemClick(itemId: Int, position: Int, category: Category) {
        when (itemId) {
            R.id.category_menu_button_delete -> viewModel.removeCategory(category.id)
        }
    }
}