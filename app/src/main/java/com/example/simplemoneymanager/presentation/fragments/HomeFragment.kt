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
import com.example.simplemoneymanager.common.Format
import com.example.simplemoneymanager.databinding.FragmentHomeBinding
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.presentation.SimpleMoneyManagerApp
import com.example.simplemoneymanager.presentation.recyclerViews.transactionList.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.HomeFragmentViewModel
import com.example.simplemoneymanager.presentation.viewModels.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import javax.inject.Inject

class HomeFragment : Fragment(), TransactionListAdapter.TransactionsPopupMenuItemClickListener {

    private val adapter = TransactionListAdapter(this)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<HomeFragmentViewModel>{
        viewModelFactory
    }

    private val format = Format()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    private val component by lazy { (requireActivity().application as SimpleMoneyManagerApp).component }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.background_fab_add_transaction)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTransactionFragment)
        }

        //TODO implement sorting feature
        val month = LocalDate.now().monthValue

        viewModel.getTransactionList().observe(viewLifecycleOwner) { transactionList ->
            binding.rvTransactions.adapter = adapter
            adapter.transactionList = transactionList.sortedWith(compareByDescending<TransactionEntity> { it.date }.thenByDescending { it.transactionId })
            adapter.onAccountClickListener = {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAccountDetailsFragment(
                        accountId = it.accountId
                    )
                )
            }
            adapter.onCategoryClickListener = { category ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToCategoryDetailsFragment(
                        categoryId = category.id
                    )
                )
            }
            setStatisticValues(transactionList, month)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setStatisticValues(transactionList: List<TransactionEntity>, month: Int) {
        binding.tvIncomeValue.text = format.formatCurrency(viewModel.getMonthIncome(transactionList, month))

        binding.tvExpenseValue.text = format.formatCurrency(viewModel.getMonthExpense(transactionList, month))

        viewModel.getOverallBalance().observe(viewLifecycleOwner) {
            binding.tvBalanceValue.text = format.formatCurrencyWithoutSign(it)
        }

        viewModel.getCashFlowByMonth(LocalDate.now().monthValue.toString())
            .observe(viewLifecycleOwner) {
                binding.tvCashFlowBalance.text = format.formatCurrency(it)
            }
    }

    private fun launchAddTransactionFragmentEditMode(transactionId: Long) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAddTransactionFragment(
                transactionId
            )
        )
    }

    override fun onMenuItemClick(itemId: Int, position: Int, transaction: TransactionEntity) {
        when (itemId) {
            R.id.transaction_menu_button_delete -> {
                viewModel.removeTransaction(transaction)
                Toast.makeText(requireContext(), "Transaction removed", Toast.LENGTH_SHORT)
                    .show()
                viewModel.subtractAccountBalance(transaction.account.accountId, transaction.amount)
            }

            R.id.transaction_menu_button_edit -> {
                launchAddTransactionFragmentEditMode(transaction.transactionId)
            }
        }
    }
}