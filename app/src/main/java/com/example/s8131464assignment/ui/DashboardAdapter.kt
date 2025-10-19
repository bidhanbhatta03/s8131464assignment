package com.example.s8131464assignment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.s8131464assignment.R
import com.example.s8131464assignment.network.models.DashboardItem

class DashboardAdapter(
    private val onClick: (DashboardItem) -> Unit
) : ListAdapter<DashboardItem, DashboardAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<DashboardItem>() {
        override fun areItemsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean =
            oldItem.id == newItem.id && oldItem.property1 == newItem.property1

        override fun areContentsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean =
            oldItem == newItem
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val t1: TextView = itemView.findViewById(R.id.tvProp1)
        private val t2: TextView = itemView.findViewById(R.id.tvProp2)
        private val meta: TextView = itemView.findViewById(R.id.tvMeta)
        fun bind(item: DashboardItem) {
            t1.text = item.property1 ?: ""
            t2.text = item.property2 ?: ""
            val parts = listOfNotNull(item.extra1, item.extra2, item.extra3).filter { it.isNotBlank() }
            meta.text = parts.joinToString(" • ")
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}


