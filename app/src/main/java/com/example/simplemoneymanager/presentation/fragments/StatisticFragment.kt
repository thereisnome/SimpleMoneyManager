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
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentStatisticBinding
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.category.CategoryWithTransactions
import com.example.simplemoneymanager.presentation.recyclerViews.statisticAccount.StatisticCategoryListAdapter
import com.example.simplemoneymanager.presentation.viewModels.StatisticViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import kotlin.math.absoluteValue


class StatisticFragment : Fragment() {
    private val viewModel: StatisticViewModel by viewModels()

    private val adapter = StatisticCategoryListAdapter()

    private var _binding: FragmentStatisticBinding? = null

    private lateinit var categoryWithTransactionList: List<CategoryWithTransactions>

    private lateinit var categoryList: List<Category>

    private var dateFilter = listOf<LocalDate>(
        LocalDate.now().with(firstDayOfMonth()), LocalDate.now().with(
            lastDayOfMonth()
        )
    )

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
        setupRecyclerView()
        initData()
        setupPieChart()
        setTypeButtonsClickListeners()
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
    }

    private fun initData() {
        viewModel.getCategoryWithTransactions()
            .observe(viewLifecycleOwner) { overallListCategoryWithTransactions ->

                categoryWithTransactionList =
                    overallListCategoryWithTransactions.filter { it.transactions.isNotEmpty() }
                categoryList = categoryWithTransactionList.map { it.category }

                passDataToListAdapter(categoryList, Category.INCOME)
                passDataToChart(
                    createPieData(
                        Category.INCOME,
                        viewModel.filterTransactionsByDate(categoryWithTransactionList)
                    )
                )
            }
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
        binding.buttonExpenseStatistic.setOnClickListener {
            passDataToListAdapter(categoryList, Category.EXPENSE)
            if (isFilteredListNotEmpty(Category.EXPENSE)){
                showChart()
                passDataToChart(createPieData(Category.EXPENSE, viewModel.filterTransactionsByDate(dateFilter[0], dateFilter[1], categoryWithTransactionList)))
            } else {
                showEmptyLayout()
            }
        }

        binding.buttonIncomeStatistic.setOnClickListener {
            passDataToListAdapter(categoryList, Category.INCOME)
            if (isFilteredListNotEmpty(Category.INCOME)){
                showChart()
                passDataToChart(createPieData(Category.INCOME, viewModel.filterTransactionsByDate(dateFilter[0], dateFilter[1], categoryWithTransactionList)))
            } else {
                showEmptyLayout()
            }
        }
    }

    private fun passDataToListAdapter(categoryList: List<Category>, type: Int) {
        adapter.submitList(categoryList.filter { it.categoryType == type })
        binding.rvStatisticCategory.adapter = adapter
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

    private fun showChart(){
        binding.layoutEmpty.visibility = View.GONE
        binding.pieChart.visibility = View.VISIBLE
    }

    private fun showEmptyLayout(){
        binding.layoutEmpty.visibility = View.VISIBLE
        binding.pieChart.visibility = View.GONE
    }

    private fun isFilteredListNotEmpty(type: Int):Boolean{
        return viewModel.filterTransactionsByDate(
            dateFilter[0],
            dateFilter[1],
            categoryWithTransactionList
        ).any { it.category.categoryType == type }
    }
}

