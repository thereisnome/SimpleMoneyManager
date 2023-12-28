package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentBudgetListBinding
import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.presentation.SimpleMoneyManagerApp
import com.example.simplemoneymanager.presentation.recyclerViews.budgetList.BudgetListAdapter
import com.example.simplemoneymanager.presentation.viewModels.BudgetListViewModel
import com.example.simplemoneymanager.presentation.viewModels.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class BudgetListFragment : Fragment(), BudgetListAdapter.BudgetPopupMenuItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<BudgetListViewModel>{
        viewModelFactory
    }

    private val adapter = BudgetListAdapter(this)

    private var _binding: FragmentBudgetListBinding? = null

    private val binding: FragmentBudgetListBinding
        get() = _binding ?: throw RuntimeException("FragmentBudgetListBinding is null")

    private val component by lazy { (requireActivity().application as SimpleMoneyManagerApp).component }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.background_fab_add_budget)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_budgetListFragment_to_addBudgetFragment)
        }

        viewModel.getBudgetWithTransactions()
            .observe(viewLifecycleOwner) { budgetWithTransactionsList ->
                adapter.budgetWithTransactionsList = budgetWithTransactionsList
                binding.rvBudgets.adapter = adapter
                adapter.onItemClickListener = { budget ->
                    findNavController().navigate(
                        BudgetListFragmentDirections.actionBudgetListFragmentToCategoryDetailsFragment(
                            budget.category.id
                        )
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(itemId: Int, position: Int, budget: BudgetEntity) {
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

    private fun createDeleteAccountDialogAlert(budget: BudgetEntity) {
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
                Toast.makeText(requireContext(), "Budget removed", Toast.LENGTH_LONG).show()
            }
            .show()
    }
}