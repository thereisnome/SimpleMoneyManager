package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentBudgetListBinding
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.presentation.recyclerViews.budgetList.BudgetListAdapter
import com.example.simplemoneymanager.presentation.viewModels.BudgetListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BudgetListFragment : Fragment(), BudgetListAdapter.BudgetPopupMenuItemClickListener {

    private val viewModel: BudgetListViewModel by viewModels()

    private val adapter = BudgetListAdapter(this)

    private var _binding: FragmentBudgetListBinding? = null

    private val binding: FragmentBudgetListBinding
        get() = _binding ?: throw RuntimeException("FragmentBudgetListBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_budgetListFragment_to_addBudgetFragment)
        }

        viewModel.getBudgetWithTransactions().observe(viewLifecycleOwner) {
            adapter.budgetWithTransactionsList = it
            binding.rvBudgets.adapter = adapter
            adapter.onItemClickListener = {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(itemId: Int, position: Int, budget: Budget) {
        when (itemId) {
            R.id.category_menu_button_delete -> createDeleteAccountDialogAlert(budget)
            R.id.category_menu_button_edit -> {
                findNavController().navigate(
                    BudgetListFragmentDirections.actionBudgetListFragmentToAddBudgetFragment(
                        budget.budgetId
                    )
                )
            }
        }
    }

    private fun createDeleteAccountDialogAlert(budget: Budget) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                resources.getString(
                    R.string.delete_budget_title,
                    budget.category.categoryName
                )
            )
            .setMessage(resources.getString(R.string.delete_budget_dialog_description))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                viewModel.removeBudget(budget.budgetId)
            }
            .show()
    }
}