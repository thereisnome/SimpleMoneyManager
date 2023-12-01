package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentHomeBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.transactionList.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.HomeFragmentViewModel
import java.time.LocalDate

class HomeFragment : Fragment(), TransactionListAdapter.TransactionsPopupMenuItemClickListener {

    private val adapter = TransactionListAdapter(this)

    private val viewModel: HomeFragmentViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setStatisticValues()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setStatisticValues() {
        viewModel.getOverallIncome().observe(viewLifecycleOwner){
            binding.tvIncomeValue.text = Transaction.formatCurrency(it)
        }

        viewModel.getOverallExpense().observe(viewLifecycleOwner){
            binding.tvExpenseValue.text = Transaction.formatCurrency(it)
        }

        viewModel.getOverallBalance().observe(viewLifecycleOwner){
            binding.tvBalanceValue.text = Transaction.formatCurrencyWithoutSign(it)
        }

        viewModel.getCashFlowByMonth(LocalDate.now().monthValue.toString()).observe(viewLifecycleOwner){
            binding.tvCashFlowBalance.text = Transaction.formatCurrency(it)
        }
    }

    private fun setupRecyclerView() {
        viewModel.getTransactionList().observe(viewLifecycleOwner){transactionList ->
            binding.rvTransactions.adapter = adapter
            adapter.transactionList = transactionList.sortedByDescending { it.transactionId }
            adapter.onAccountClickListener = {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAccountDetailsFragment(accountId = it.accountId))
            }
        }
    }

    private fun launchAddTransactionFragmentEditMode(transactionId: Long) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddTransactionFragment(transactionId))
    }

    override fun onMenuItemClick(itemId: Int, position: Int, transaction: Transaction) {
        when (itemId) {
            R.id.transaction_menu_button_delete -> {
                viewModel.removeTransaction(transaction)
                viewModel.subtractAccountBalance(transaction.account.accountId, transaction.amount)
            }
            R.id.transaction_menu_button_edit -> {
                launchAddTransactionFragmentEditMode(transaction.transactionId)
            }
        }
    }
}