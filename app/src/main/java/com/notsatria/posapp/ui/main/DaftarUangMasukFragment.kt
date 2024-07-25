package com.notsatria.posapp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.notsatria.posapp.R
import com.notsatria.posapp.databinding.FragmentDaftarUangMasukBinding
import com.notsatria.posapp.models.Footer
import com.notsatria.posapp.models.Header
import com.notsatria.posapp.models.Item
import com.notsatria.posapp.models.Transaction
import com.notsatria.posapp.ui.adapter.DaftarUangMasukAdapter
import com.notsatria.posapp.ui.adapter.TableAdapter


class DaftarUangMasukFragment : Fragment() {
    private var _binding: FragmentDaftarUangMasukBinding? = null
    private val binding get() = _binding!!

    private val transactions: List<Transaction> = listOf(
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            "Rp 100.000",
            "25 Maret 2024"
        ),
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            "Rp 100.000",
            "25 Maret 2024"
        ),
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            "Rp 100.000",
            "26 Maret 2024"
        ),
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            "Rp 100.000",
            "26 Maret 2024"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarUangMasukBinding.inflate(inflater, container, false)

        showUangMasukRvPortrait(transactions)

        return binding.root
    }

    private fun showUangMasukRvPortrait(transactionList: List<Transaction>) {
        val orientation = resources.configuration.orientation

        binding.rvDaftarUangMasuk!!.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                adapter = DaftarUangMasukAdapter(transactionList)
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val data = mapTransactionsToTableData(transactionList)
                adapter = TableAdapter(data)
            }

        }
    }

    private fun mapTransactionsToTableData(transactions: List<Transaction>): List<Any> {
        val data = mutableListOf<Any>()
        var currentHeader: Header? = null
        var currentTotal = 0

        transactions.forEach { transaction ->
            // Add a new header if the date is different
            if (currentHeader?.date != transaction.date) {
                currentHeader?.let {
                    // Add the footer for the previous date
                    data.add(Footer("Rp ${currentTotal}"))
                }
                currentHeader = Header(transaction.date)
                currentHeader?.let {
                    data.add(it)
                }
                currentTotal = 0 // Reset the total for the new date
            }

            // Add the transaction as an item
            val item = Item(
                time = transaction.time,
                to = transaction.to,
                from = transaction.from,
                description = transaction.description,
                amount = transaction.amount
            )
            data.add(item)

            // Update the total for the current date
            currentTotal += transaction.amount.replace("Rp ", "").replace(".", "").toInt()
        }

        // Add the footer for the last date
        currentHeader?.let {
            data.add(Footer("Rp ${currentTotal}"))
        }

        return data
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}