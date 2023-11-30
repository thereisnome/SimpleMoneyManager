package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.databinding.FragmentAccountListBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.AccountDetailsListAdapter
import com.example.simplemoneymanager.presentation.viewModels.AccountListViewModel
import java.time.LocalDate

class AccountListFragment : Fragment() {

    private val viewModel: AccountListViewModel by viewModels()

    private var _binding: FragmentAccountListBinding? = null
    private val binding: FragmentAccountListBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTransactionListWithAccountsByMonth(LocalDate.now().monthValue.toString()).observe(viewLifecycleOwner){ accountTransactionsMap ->
            val accountList = accountTransactionsMap.keys.toList().sortedBy { it.accountId }
            val transactionList = accountTransactionsMap.values.flatten().toList()
            val adapter = AccountDetailsListAdapter(transactionList)
            adapter.submitList(accountList)
            binding.rvAccountDetails.adapter = adapter
        }

        viewModel.getOverallBalance().observe(viewLifecycleOwner) {
            binding.tvTotalAmount.text = Transaction.formatCurrency(it)
        }
    }
}