package com.example.simplemoneymanager.presentation.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAccountDetailsBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.transactionList.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.AccountDetailsFragmentViewModel

class AccountDetailsFragment : Fragment(),
    TransactionListAdapter.TransactionsPopupMenuItemClickListener {

    private val args by navArgs<AccountDetailsFragmentArgs>()

    private val adapter = TransactionListAdapter(this)

    private val viewModel: AccountDetailsFragmentViewModel by viewModels()

    private var _binding: FragmentAccountDetailsBinding? = null
    private val binding: FragmentAccountDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAccountById(args.accountId).observe(viewLifecycleOwner) { account ->
            binding.tvAccountName.text = account.accountName
            binding.tvAccountBalance.text = Transaction.formatCurrencyWithoutSign(account.balance)
            binding.accountDetailsLayout.backgroundTintList =
                ColorStateList.valueOf(account.accountColor.toColorInt())

            val contrast = ColorUtils.calculateContrast(
                binding.tvAccountName.currentHintTextColor,
                account.accountColor.toColorInt()
            )

            if (contrast < 1.5f) {
                binding.tvAccountName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                binding.tvAccountBalance.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
        }

        viewModel.getTransactionList().observe(viewLifecycleOwner) { transactionList ->
            val transactionListByAccount =
                transactionList.filter { it.account.accountId == args.accountId }
            adapter.transactionList = transactionListByAccount.sortedByDescending { it.transactionId }
            binding.rvTransactions.adapter = adapter

            val incomeSum = transactionListByAccount.filter { it.type == Transaction.INCOME }
                .sumOf { it.amount }
            val expenseSum = transactionListByAccount.filter { it.type == Transaction.EXPENSE }
                .sumOf { it.amount }

            val incomeTransactionCount =
                transactionListByAccount.filter { it.type == Transaction.INCOME }.size.toString()
            val expenseTransactionCount =
                transactionListByAccount.filter { it.type == Transaction.EXPENSE }.size.toString()

            binding.tvIncomeValue.text = Transaction.formatCurrency(incomeSum)
            binding.tvExpenseValue.text = Transaction.formatCurrency(expenseSum)

            binding.tvIncomeTransactionsCountValue.text = incomeTransactionCount
            if (incomeTransactionCount.toInt() > 1) {
                binding.tvIncomeTransactionsCountLabel.text =
                    requireContext().getString(R.string.transactions)
            } else binding.tvIncomeTransactionsCountLabel.text =
                requireContext().getString(R.string.transaction)

            binding.tvExpenseTransactionsCountValue.text = expenseTransactionCount
            if (incomeTransactionCount.toInt() > 1) {
                binding.tvExpenseTransactionsCountLabel.text =
                    requireContext().getString(R.string.transactions)
            } else binding.tvExpenseTransactionsCountLabel.text =
                requireContext().getString(R.string.transaction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchAddTransactionFragmentEditMode(transactionId: Long) {
        findNavController().navigate(
            AccountDetailsFragmentDirections.actionAccountDetailsFragmentToAddTransactionFragment(
                transactionId
            )
        )
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