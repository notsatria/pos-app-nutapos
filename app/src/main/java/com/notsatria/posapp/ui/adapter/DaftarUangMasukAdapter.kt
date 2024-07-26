package com.notsatria.posapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.notsatria.posapp.databinding.ItemUangMasukCardBinding
import com.notsatria.posapp.databinding.LayoutHeaderPotraitBinding
import com.notsatria.posapp.models.Header
import com.notsatria.posapp.models.Item
import com.notsatria.posapp.utils.formatRupiah

class DaftarUangMasukAdapter(
    private val data: List<Any>,
    private val onDeleteClickListener: (Int) -> Unit,
    private val onEditClickListener: (Item) -> Unit,
    private val onLihatClickListener: (Item) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is Header) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = LayoutHeaderPotraitBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }

            TYPE_ITEM -> {
                val binding = ItemUangMasukCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(data[position] as Header)
            is ItemViewHolder -> holder.bind(data[position] as Item)
        }
    }

    override fun getItemCount(): Int = data.size

    inner class HeaderViewHolder(private val binding: LayoutHeaderPotraitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: Header) {
            binding.tvDate.text = header.date
            binding.tvAmount.text = formatRupiah(header.amount!!)
        }
    }

    inner class ItemViewHolder(private val binding: ItemUangMasukCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            with(binding) {
                tvMasukKe.text = "Dari ${item.from} ke ${item.to}"
                tvJam.text = item.time
                tvJumlah.text = formatRupiah(item.amount)
                tvKeterangan.text = item.description

                btnDelete.setOnClickListener {
                    onDeleteClickListener(item.id)
                    notifyItemRemoved(adapterPosition)
                }

                btnEdit.setOnClickListener {
                    onEditClickListener(item)
                }

                if (item.imageUri == "null") {
                    btnLihatFoto.visibility = View.GONE
                }

                btnLihatFoto.setOnClickListener {
                    onLihatClickListener(item)
                }
            }
        }
    }
}
