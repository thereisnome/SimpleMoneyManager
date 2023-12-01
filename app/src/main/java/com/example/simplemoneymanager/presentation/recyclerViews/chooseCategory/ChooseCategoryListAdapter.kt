package com.example.simplemoneymanager.presentation.recyclerViews.chooseCategory

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.CategoryItemBinding
import com.example.simplemoneymanager.domain.category.Category

class ChooseCategoryListAdapter(private val itemClickListener: CategoryPopupMenuItemClickListener) :
    ListAdapter<Category, ChooseCategoryViewHolder>(ChooseCategoryListDiffCallback()) {

    var onItemClickListener: ((Category) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCategoryViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChooseCategoryViewHolder((binding))
    }

    override fun onBindViewHolder(holder: ChooseCategoryViewHolder, position: Int) {
        val category = getItem(position)

        val contrast = ColorUtils.calculateContrast(
            holder.binding.tvCategoryName.currentHintTextColor,
            category.categoryColor.toColorInt()
        )

        if (contrast < 1.5f){
            holder.binding.tvCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }

        holder.binding.tvCategoryName.text = category.categoryName
        holder.binding.tvCategoryName.backgroundTintList =
            ColorStateList.valueOf(category.categoryColor.toColorInt())

        holder.binding.tvCategoryName.setOnClickListener {
            onItemClickListener?.invoke(category)
        }

        holder.binding.tvCategoryName.setOnLongClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.itemView)
            popupMenu.inflate(R.menu.category_bottom_sheet_dialog_fragment_context_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val itemPosition = holder.adapterPosition
                itemClickListener.onMenuItemClick(
                    menuItem.itemId,
                    itemPosition,
                    currentList[position]
                )
                true
            }
            popupMenu.show()
            true
        }
    }

    interface CategoryPopupMenuItemClickListener {
        fun onMenuItemClick(itemId: Int, position: Int, category: Category)
    }
}