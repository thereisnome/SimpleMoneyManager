package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.common.DateFilter
import com.example.simplemoneymanager.databinding.FragmentStatisticBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.CategoryWithTransactions
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.statisticAccount.StatisticCategoryListAdapter
import com.example.simplemoneymanager.presentation.recyclerViews.transactionList.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.StatisticViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.absoluteValue

class StatisticFragment : Fragment(), FilterBottomSheetDialogFragment.DataPassListener,
    TransactionListAdapter.TransactionsPopupMenuItemClickListener {
    private val viewModel: StatisticViewModel by viewModels()

    private val categoryAdapter = StatisticCategoryListAdapter()

    private val transactionsAdapter = TransactionListAdapter(this)

    private var _binding: FragmentStatisticBinding? = null

    private lateinit var categoryWithTransactionList: List<CategoryWithTransactions>

    private var dateFilter: DateFilter = DateFilter.getDefaultDateFilter()

    private var statisticType = Category.INCOME

    private val binding: FragmentStatisticBinding
        get() = _binding ?: throw RuntimeException("FragmentStatisticBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFab()
        setupRecyclerView()
        initData()
        setupPieChart()
        setTypeButtonsClickListeners()
    }

    private fun setupFab() {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.foreground =
            ContextCompat.getDrawable(requireContext(), R.drawable.background_fab_filter)
        fab.setOnClickListener {
            showFilterBottomSheetFragment()
        }
    }

    private fun showFilterBottomSheetFragment() {
        val filterFragment = FilterBottomSheetDialogFragment(dateFilter)
        filterFragment.setDataPassListener(this)
        filterFragment.show(childFragmentManager, "TEST")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val flexboxLayoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvStatisticCategory.layoutManager = flexboxLayoutManager

        val linearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvStatisticTransactions.layoutManager = linearLayoutManager
    }

    private fun initData() {
        viewModel.getCategoryWithTransactions()
            .observe(viewLifecycleOwner) { overallListCategoryWithTransactions ->

                categoryWithTransactionList =
                    overallListCategoryWithTransactions.filter { it.transactions.isNotEmpty() }

                passDataToListAdapter(categoryWithTransactionList, Category.INCOME)
                updateChart()
            }
    }

    private fun filterCategoryList(
        categoryWithTransactionsList: List<CategoryWithTransactions>,
        type: Int
    ): List<Category> {
        return viewModel.filterCategoryWithTransactionsByDate(
            dateFilter.startDate,
            dateFilter.endDate,
            categoryWithTransactionsList
        ).map { it.category }.filter { it.categoryType == type }
    }

    private fun filterTransactionList(
        categoryWithTransactionsList: List<CategoryWithTransactions>,
        type: Int
    ): List<Transaction> {
        return viewModel.filterTransactionsByDate(dateFilter.startDate,
            dateFilter.endDate,
            categoryWithTransactionsList.flatMap { it.transactions }.filter { it.type == type })
            .sortedWith(compareByDescending<Transaction> { it.date }.thenByDescending { it.transactionId })
    }

    private fun setupPieChart() {
        binding.pieChart.setHoleColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.md_theme_dark_surface
            )
        )
        binding.pieChart.description.isEnabled = false
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.minAngleForSlices = 20f
        binding.pieChart.setEntryLabelTypeface(
            ResourcesCompat.getFont(
                requireContext(),
                R.font.open_sans_semibold
            )
        )
        binding.pieChart.setEntryLabelTextSize(15f)
    }

    private fun setTypeButtonsClickListeners() {
        binding.buttonIncomeStatistic.setOnClickListener {
            statisticType = Category.INCOME
            passDataToListAdapter(categoryWithTransactionList, Category.INCOME)
            updateChart()
        }

        binding.buttonExpenseStatistic.setOnClickListener {
            statisticType = Category.EXPENSE
            passDataToListAdapter(categoryWithTransactionList, Category.EXPENSE)
            updateChart()
        }
    }

    private fun passDataToListAdapter(
        categoryWithTransactionsList: List<CategoryWithTransactions>,
        type: Int
    ) {
        categoryAdapter.submitList(filterCategoryList(categoryWithTransactionsList, type))
        transactionsAdapter.transactionList =
            filterTransactionList(categoryWithTransactionsList, type)
        binding.rvStatisticCategory.adapter = categoryAdapter
        binding.rvStatisticTransactions.adapter = transactionsAdapter
    }

    private fun passDataToChart(pieData: PieData) {
        binding.pieChart.data = pieData
        binding.pieChart.animateXY(500, 500, Easing.EaseInOutCirc)
        binding.pieChart.invalidate()
    }

    //    categoryWithTransactionsList should be filtered by date
    private fun createPieData(
        type: Int,
        categoryWithTransactionsList: List<CategoryWithTransactions>
    ): PieData {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()
        val filteredCategoryWithTransactionsList =
            categoryWithTransactionsList.filter { it.category.categoryType == type }

        for (item in filteredCategoryWithTransactionsList) {
            entries.add(
                PieEntry(
                    item.transactions.sumOf { it.amount.absoluteValue }.toFloat(),
                    item.category.categoryName
                )
            )
            colors.add(item.category.categoryColor.toColorInt())
        }

        val pieDataSet = PieDataSet(entries, requireContext().getString(R.string.statistics))

        pieDataSet.valueTextSize = 20f
        pieDataSet.sliceSpace = 10F
        pieDataSet.valueTypeface =
            ResourcesCompat.getFont(requireContext(), R.font.open_sans_semibold)
        pieDataSet.colors = colors

        return PieData(pieDataSet)
    }

    private fun showChart() {
        binding.layoutEmpty.visibility = View.GONE
        binding.pieChart.visibility = View.VISIBLE
        binding.rvStatisticCategory.visibility = View.VISIBLE
    }

    private fun showEmptyLayout() {
        binding.layoutEmpty.visibility = View.VISIBLE
        binding.pieChart.visibility = View.GONE
        binding.rvStatisticCategory.visibility = View.GONE
    }

    private fun updateChart() {
        if (isFilteredListNotEmpty(statisticType)) {
            showChart()
            passDataToChart(
                createPieData(
                    statisticType,
                    viewModel.filterCategoryWithTransactionsByDate(
                        dateFilter.startDate,
                        dateFilter.endDate,
                        categoryWithTransactionList
                    )
                )
            )
        } else {
            showEmptyLayout()
        }
    }

    private fun launchAddTransactionFragmentEditMode(transactionId: Long) {
        findNavController().navigate(
            StatisticFragmentDirections.actionStatisticFragmentToAddTransactionFragment(
                transactionId
            )
        )
    }

    private fun isFilteredListNotEmpty(type: Int): Boolean {
        return viewModel.filterCategoryWithTransactionsByDate(
            dateFilter.startDate,
            dateFilter.endDate,
            categoryWithTransactionList
        ).any { it.category.categoryType == type }
    }

    override fun onDateFilterPassed(dateFilter: DateFilter) {
        this.dateFilter = dateFilter
        updateChart()
        passDataToListAdapter(categoryWithTransactionList, statisticType)
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

