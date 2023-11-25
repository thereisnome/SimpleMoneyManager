package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentHistoryBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.HistoryViewModel

class HistoryFragment : Fragment(), TransactionListAdapter.TransactionsPopupMenuItemClickListener {

    private val adapter = TransactionListAdapter(this)

    private val viewModel: HistoryViewModel by viewModels()

    private var overallExpense = 0
    private var overallIncome = 0
    private var overallBalance = 0
    private lateinit var transactionList: List<Transaction>

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        viewModel.getTransactionList().observe(viewLifecycleOwner) {
            transactionList = it
            setupRecyclerView()
            setStatisticValues()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddTransaction.setOnClickListener {
            launchAddTransactionFragment()
        }
        binding.fabAddTransaction.setOnLongClickListener {
            viewModel.removeAllTransactions()
            viewModel.clearAllAccountBalances()
            true
        }
    }

    private fun setStatisticValues() {
        val incomeTransactionList = transactionList.filter { it.type == Transaction.INCOME }
        overallIncome = viewModel.getOverallBalance(incomeTransactionList)
        binding.tvIncomeValue.text = Transaction.formatIncome(overallIncome)

        val expenseTransactionList = transactionList.filter { it.type == Transaction.EXPENSE }
        overallExpense = viewModel.getOverallBalance(expenseTransactionList)
        binding.tvExpenseValue.text = Transaction.formatExpense(overallExpense)

        overallBalance = viewModel.getOverallBalance(transactionList)

        if (overallBalance >= 0) {
            binding.tvBalanceValue.text = Transaction.formatIncome(overallBalance)
        } else binding.tvBalanceValue.text = Transaction.formatExpense(overallBalance)
    }

    private fun setupRecyclerView() {
        binding.rvTransactions.adapter = adapter
        adapter.transactionList = transactionList.sortedByDescending { it.transactionId }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchAddTransactionFragment() {
        findNavController().navigate(R.id.action_historyFragment_to_addTransactionFragment)
    }

    override fun onMenuItemClick(itemId: Int, position: Int, transaction: Transaction) {
        when (itemId) {
            R.id.transaction_menu_button_delete -> {
                viewModel.removeTransaction(transaction)
                viewModel.subtractAccountBalance(transaction.account, transaction.amount)
            }
        }
    }
}