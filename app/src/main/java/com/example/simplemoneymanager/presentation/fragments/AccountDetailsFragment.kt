package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.common.Format
import com.example.simplemoneymanager.data.database.models.TransactionDbModel
import com.example.simplemoneymanager.databinding.FragmentAccountDetailsBinding
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import com.example.simplemoneymanager.presentation.SimpleMoneyManagerApp
import com.example.simplemoneymanager.presentation.recyclerViews.transactionList.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.AccountDetailsFragmentViewModel
import com.example.simplemoneymanager.presentation.viewModels.ViewModelFactory
import javax.inject.Inject
import kotlin.math.absoluteValue

class AccountDetailsFragment : Fragment(),
    TransactionListAdapter.TransactionsPopupMenuItemClickListener {

    private val args by navArgs<AccountDetailsFragmentArgs>()

    private val adapter = TransactionListAdapter(this)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<AccountDetailsFragmentViewModel>{
        viewModelFactory
    }

    private val format = Format()

    private var _binding: FragmentAccountDetailsBinding? = null
    private val binding: FragmentAccountDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    private val component by lazy { (requireActivity().application as SimpleMoneyManagerApp).component }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
            binding.tvAccountBalance.text = format.formatCurrencyWithoutSign(account.balance)
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

            setupRecyclerView(transactionListByAccount)

            val incomeSum = transactionListByAccount.filter { it.type == TransactionDbModel.INCOME }
                .sumOf { it.amount }
            val expenseSum = transactionListByAccount.filter { it.type == TransactionDbModel.EXPENSE }
                .sumOf { it.amount }

            val incomeTransactionCount =
                transactionListByAccount.filter { it.type == TransactionDbModel.INCOME }.size.toString()
            val expenseTransactionCount =
                transactionListByAccount.filter { it.type == TransactionDbModel.EXPENSE }.size.toString()

            binding.tvIncomeValue.text = format.formatCurrencyWithoutSign(incomeSum.absoluteValue)
            binding.tvExpenseValue.text = format.formatCurrencyWithoutSign(expenseSum.absoluteValue)

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

    private fun setupRecyclerView(transactionListByAccount: List<TransactionEntity>){
        val linearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvTransactions.layoutManager = linearLayoutManager
        binding.rvTransactions.adapter = adapter
        adapter.onCategoryClickListener = { category ->
            findNavController().navigate(
                AccountDetailsFragmentDirections.actionAccountDetailsFragmentToCategoryDetailsFragment(
                    categoryId = category.id
                )
            )
        }

        adapter.transactionList =
            transactionListByAccount.sortedWith(compareByDescending<TransactionEntity> { it.date }.thenByDescending { it.transactionId })
    }

    private fun launchAddTransactionFragmentEditMode(transactionId: Long) {
        findNavController().navigate(
            AccountDetailsFragmentDirections.actionAccountDetailsFragmentToAddTransactionFragment(
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