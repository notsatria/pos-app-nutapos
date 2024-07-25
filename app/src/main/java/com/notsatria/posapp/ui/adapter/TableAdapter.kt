package com.notsatria.posapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.notsatria.posapp.R
import com.notsatria.posapp.models.Footer
import com.notsatria.posapp.models.Header
import com.notsatria.posapp.models.Item
import com.notsatria.posapp.utils.formatRupiah

class TableAdapter(private val data: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Header -> TYPE_HEADER
            is Item -> TYPE_ITEM
            is Footer -> TYPE_FOOTER
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_header_land, parent, false)
                HeaderViewHolder(view)
            }

            TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.layout_item_land, parent, false)
                ItemViewHolder(view)
            }

            TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_footer_land, parent, false)
                FooterViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(data[position] as Header)
            is ItemViewHolder -> holder.bind(data[position] as Item)
            is FooterViewHolder -> holder.bind(data[position] as Footer)
        }
    }

    override fun getItemCount(): Int = data.size

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        fun bind(header: Header) {
            tvDate.text = header.date
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvJam: TextView = itemView.findViewById(R.id.tvWaktu)
        private val tvMasukKe: TextView = itemView.findViewById(R.id.tvMasukKe)
        private val tvTerimaDari: TextView = itemView.findViewById(R.id.tvTerimaDari)
        private val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        private val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        fun bind(item: Item) {
            tvJam.text = item.time
            tvMasukKe.text = item.to
            tvTerimaDari.text = item.from
            tvKeterangan.text = item.description
            tvJumlah.text = formatRupiah(item.amount)
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTotalAmount: TextView = itemView.findViewById(R.id.tvTotalAmount)
        fun bind(footer: Footer) {
            tvTotalAmount.text = footer.total
        }
    }
}
