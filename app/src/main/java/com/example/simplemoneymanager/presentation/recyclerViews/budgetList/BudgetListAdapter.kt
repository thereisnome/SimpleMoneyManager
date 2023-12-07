package com.example.simplemoneymanager.presentation.recyclerViews.budgetList

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.BudgetListItemBinding
import com.example.simplemoneymanager.domain.budget.Budget
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactions
import com.example.simplemoneymanager.domain.transaction.Transaction
import java.time.LocalDate
import kotlin.math.absoluteValue

class BudgetListAdapter(private val itemClickListener: BudgetPopupMenuItemClickListener) :
    RecyclerView.Adapter<BudgetViewHolder>() {

    var onItemClickListener: ((Budget) -> Unit)? = null

    var budgetWithTransactionsList = listOf<BudgetWithTransactions>()
        set(value) {
            val callback = BudgetListDiffCallback(budgetWithTransactionsList.map { it.budget },
                value.map { it.budget })
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding =
            BudgetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder((binding))
    }

    override fun getItemCount(): Int {
        return budgetWithTransactionsList.map { it.budget }.size
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = budgetWithTransactionsList.map { it.budget }[position]
        val category = budget.category
        val transactionList =
            budgetWithTransactionsList[position].transactions.filter { it.date.monthValue == LocalDate.now().monthValue }
        val currentSpentValue = transactionList.sumOf { it.amount.absoluteValue }

        val progress = (currentSpentValue / budget.maxValue * 100).toInt()
        holder.binding.budgetProgressIndicator.progress = progress
        holder.binding.budgetProgressIndicator.setIndicatorColor(category.categoryColor.toColorInt())

        if (progress > 100) {
            holder.binding.budgetProgressIndicator.setIndicatorColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.md_theme_dark_errorContainer
                )
            )
        }

        val contrast = ColorUtils.calculateContrast(
            holder.binding.tvLeftToSpendValue.currentHintTextColor,
            holder.binding.budgetProgressIndicator.indicatorColor[0]
        )

        if (contrast < 1.5f && progress > 0) {
            holder.binding.tvLeftToSpendLabel.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
            holder.binding.tvLeftToSpendValue.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
            holder.binding.tvBudgetSpend.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
            holder.binding.ivState.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.binding.tvBudgetName.text = category.categoryName
        holder.binding.tvBudgetValue.text = Transaction.formatCurrencyWithoutSign(budget.maxValue)
        val leftToSpendValue = budget.maxValue - currentSpentValue
        if (leftToSpendValue >= 0) {
            holder.binding.tvLeftToSpendValue.text =
                Transaction.formatCurrencyWithoutSign(leftToSpendValue)
        } else {
            holder.binding.tvLeftToSpendLabel.text =
                holder.itemView.context.getString(R.string.limit_exceeded)
            holder.binding.tvLeftToSpendValue.text =
                Transaction.formatCurrencyWithoutSign(leftToSpendValue.absoluteValue)
        }

        holder.binding.tvBudgetSpend.text = holder.itemView.context.getString(
            R.string.budget_spend,
            Transaction.formatCurrencyWithoutCurrencySigh(currentSpentValue),
            Transaction.formatCurrencyWithoutSign(budget.maxValue)
        )

        val budgetIcon = if (progress <= 100) {
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_done)!!
        } else ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_warning)!!

        holder.binding.ivState.setImageDrawable(budgetIcon)

        holder.binding.budgetListItemLayout.setOnClickListener {
            onItemClickListener?.invoke(budget)
        }

        holder.binding.budgetListItemLayout.setOnLongClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.itemView)
            popupMenu.inflate(R.menu.category_bottom_sheet_dialog_fragment_context_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val itemPosition = holder.adapterPosition
                itemClickListener.onMenuItemClick(
                    menuItem.itemId,
                    itemPosition,
                    budget
                )
                true
            }
            popupMenu.show()
            true
        }
    }

    interface BudgetPopupMenuItemClickListener {
        fun onMenuItemClick(itemId: Int, position: Int, budget: Budget)
    }
}