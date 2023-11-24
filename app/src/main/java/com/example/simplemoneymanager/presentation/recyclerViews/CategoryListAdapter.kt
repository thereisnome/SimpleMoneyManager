package com.example.simplemoneymanager.presentation.recyclerViews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.CategoryItemBinding
import com.example.simplemoneymanager.domain.category.Category

class CategoryListAdapter(private val itemClickListener: CategoryPopupMenuItemClickListener) :
    ListAdapter<Category, CategoryViewHolder>(CategoryListDiffCallback()) {

    var onItemClickListener: ((Category) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder((binding))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.binding.tvCategoryName.text = category.categoryName

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