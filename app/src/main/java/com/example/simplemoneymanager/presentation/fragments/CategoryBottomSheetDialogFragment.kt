package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentCategoryBottomSheetBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.presentation.recyclerViews.chooseCategory.ChooseCategoryListAdapter
import com.example.simplemoneymanager.presentation.viewModels.CategoryBottomSheetViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottomSheetDialogFragment private constructor(private val categoryType: Int) :
    BottomSheetDialogFragment(), ChooseCategoryListAdapter.CategoryPopupMenuItemClickListener {

    private var dataPassListener: DataPassListener? = null

    private val adapter = ChooseCategoryListAdapter(this)

    private val viewModel: CategoryBottomSheetViewModel by viewModels()

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

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvCategories.layoutManager = flexboxLayoutManager

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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