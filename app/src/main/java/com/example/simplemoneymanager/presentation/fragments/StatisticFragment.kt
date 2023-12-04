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
import kotlin.math.absoluteValue

class StatisticFragment : Fragment() {
    private val viewModel: StatisticViewModel by viewModels()

    private val adapter = StatisticCategoryListAdapter()

    private var _binding: FragmentStatisticBinding? = null

    private val incomeCategoryList = mutableListOf<Category>()
    private val expenseCategoryList = mutableListOf<Category>()

    private lateinit var expensePieData: PieData
    private lateinit var incomePieData: PieData

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

        setupPieChart()
        createPieDataSets()
        setupRecyclerView()

        binding.buttonExpenseStatistic.setOnClickListener {
            adapter.submitList(expenseCategoryList)
            binding.rvStatisticCategory.adapter = adapter
            passDataToChart(expensePieData)
            binding.pieChart.animateXY(500,500, Easing.EaseInOutCirc)
        }

        binding.buttonIncomeStatistic.setOnClickListener {
            adapter.submitList(incomeCategoryList)
            binding.rvStatisticCategory.adapter = adapter
            passDataToChart(incomePieData)
            binding.pieChart.animateXY(500,500, Easing.EaseInOutCirc)
        }
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

    private fun setupPieChart() {
        binding.pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.md_theme_dark_surface))
        binding.pieChart.description.isEnabled = false
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.minAngleForSlices = 20f
        binding.pieChart.setEntryLabelTypeface(ResourcesCompat.getFont(requireContext(), R.font.open_sans_semibold))
        binding.pieChart.setEntryLabelTextSize(15f)
    }

    private fun passDataToChart(pieData: PieData){
        binding.pieChart.data = pieData
        binding.pieChart.invalidate()
    }

    private fun createPieDataSets() {
        viewModel.getCategoryWithTransactions(Category.INCOME).observe(viewLifecycleOwner) {

            incomeCategoryList.addAll(it.filter { it.transactions.isNotEmpty() }.map { it.category })
            adapter.submitList(incomeCategoryList)
            binding.rvStatisticCategory.adapter = adapter

            val incomeEntries = ArrayList<PieEntry>()
            val incomeColors = ArrayList<Int>()

            for (item in it) {
                if (item.transactions.isNotEmpty()){
                    incomeEntries.add(PieEntry(item.transactions.filter { it.date.monthValue == LocalDate.now().monthValue }
                        .sumOf { it.amount }.toFloat(), item.category.categoryName))
                    incomeColors.add(item.category.categoryColor.toColorInt())
                }
            }

            val incomePieDataSet =
                PieDataSet(incomeEntries, requireContext().getString(R.string.statistics))

            incomePieDataSet.valueTextSize = 20f
            incomePieDataSet.sliceSpace = 10F
            incomePieDataSet.valueTypeface = ResourcesCompat.getFont(requireContext(), R.font.open_sans_semibold)
            incomePieDataSet.colors = incomeColors
            incomePieData = PieData(incomePieDataSet)

            binding.pieChart.data = incomePieData
            binding.pieChart.animateXY(500,500, Easing.EaseInOutCirc)
            binding.pieChart.invalidate()
        }

        viewModel.getCategoryWithTransactions(Category.EXPENSE).observe(viewLifecycleOwner) {

            expenseCategoryList.addAll(it.filter { it.transactions.isNotEmpty() }.map { it.category })

            val expenseEntries = ArrayList<PieEntry>()
            val expenseColors = ArrayList<Int>()

            for (item in it) {
                if (item.transactions.isNotEmpty()){
                    expenseEntries.add(PieEntry(item.transactions.filter { it.date.monthValue == LocalDate.now().monthValue }
                        .sumOf { it.amount.absoluteValue }.toFloat(), item.category.categoryName))
                    expenseColors.add(item.category.categoryColor.toColorInt())
                }
            }

            val expensePieDataSet =
                PieDataSet(expenseEntries, requireContext().getString(R.string.statistics))

            expensePieDataSet.valueTextSize = 20f
            expensePieDataSet.sliceSpace = 10F
            expensePieDataSet.valueTypeface = ResourcesCompat.getFont(requireContext(), R.font.open_sans_semibold)
            expensePieDataSet.colors = expenseColors

            expensePieData = PieData(expensePieDataSet)
        }
    }
}

