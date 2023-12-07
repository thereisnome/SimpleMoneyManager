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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentCategoryDetailsBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.transactionList.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.CategoryDetailsFragmentViewModel
import kotlin.math.absoluteValue

class CategoryDetailsFragment : Fragment(),
    TransactionListAdapter.TransactionsPopupMenuItemClickListener {

    private val args by navArgs<CategoryDetailsFragmentArgs>()

    private val adapter = TransactionListAdapter(this)

    private val viewModel: CategoryDetailsFragmentViewModel by viewModels()

    private var _binding: FragmentCategoryDetailsBinding? = null
    private val binding: FragmentCategoryDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategoryById(args.categoryId).observe(viewLifecycleOwner) { category ->
            binding.tvCategoryName.text = category.categoryName
            binding.tvTypeLabel.text = if (category.categoryType == Category.INCOME) {
                requireContext().getString(R.string.income)
            } else requireContext().getString(R.string.expense)

            binding.categoryDetailsLayout.backgroundTintList =
                ColorStateList.valueOf(category.categoryColor.toColorInt())

            val contrast = ColorUtils.calculateContrast(
                binding.tvCategoryName.currentHintTextColor,
                category.categoryColor.toColorInt()
            )

            if (contrast < 1.5f) {
                binding.tvCategoryName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
        }

        viewModel.getTransactionList().observe(viewLifecycleOwner) { transactionList ->
            val transactionListByCategory =
                transactionList.filter { it.category.id == args.categoryId }

            adapter.transactionList =
                transactionListByCategory.sortedByDescending { it.transactionId }

            val linearLayoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            binding.rvTransactions.layoutManager = linearLayoutManager
            binding.rvTransactions.adapter = adapter
            adapter.onAccountClickListener = { account ->
                findNavController().navigate(
                    CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToAccountDetailsFragment(
                        accountId = account.accountId
                    )
                )
            }

            val transactionsSum = transactionListByCategory.sumOf { it.amount }

            val incomeTransactionCount = transactionListByCategory.size.toString()

            binding.tvTypeValue.text = Transaction.formatCurrencyWithoutSign(transactionsSum.absoluteValue)

            binding.tvTransactionsCountValue.text = incomeTransactionCount
            if (incomeTransactionCount.toInt() > 1) {
                binding.tvTransactionsCountLabel.text =
                    requireContext().getString(R.string.transactions)
            } else binding.tvTransactionsCountLabel.text =
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