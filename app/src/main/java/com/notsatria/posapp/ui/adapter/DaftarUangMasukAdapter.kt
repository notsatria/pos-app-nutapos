package com.notsatria.posapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.notsatria.posapp.databinding.ItemUangMasukCardBinding
import com.notsatria.posapp.models.Transaction

class DaftarUangMasukAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<DaftarUangMasukAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUangMasukCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(
            transactionList[position]
        )
    }

    override fun getItemCount(): Int = transactionList.size

    class ViewHolder(private val binding: ItemUangMasukCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(transaction: Transaction) {
            with(binding) {
                tvMasukKe.text = "Dari ${transaction.from} ke ${transaction.to}"
                tvJam.text = transaction.time
                tvJumlah.text = transaction.amount
                tvKeterangan.text = transaction.description
            }
        }
    }
}