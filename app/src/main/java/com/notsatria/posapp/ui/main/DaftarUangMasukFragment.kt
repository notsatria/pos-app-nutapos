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
import com.notsatria.posapp.utils.formatRupiah


class DaftarUangMasukFragment : Fragment() {
    private var _binding: FragmentDaftarUangMasukBinding? = null
    private val binding get() = _binding!!

    private val transactions: List<Transaction> = listOf(
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            100000,
            "25 Maret 2024"
        ),
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            100000,
            "25 Maret 2024"
        ),
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            100000,
            "26 Maret 2024"
        ),
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            100000,
            "26 Maret 2024"
        ),
        Transaction(
            "19:00:20",
            "Kasir Perangkat 1",
            "Bos",
            "Tambahan modal",
            100000,
            "27 Maret 2024"
        ),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarUangMasukBinding.inflate(inflater, container, false)

        showUangMasukRvPortrait(transactions)

        binding.btnBuatTransaksi!!.setOnClickListener {
            goToFragmentInputUangMasuk()
        }

        return binding.root
    }

    private fun goToFragmentInputUangMasuk() {
        val fragmentB = InputUangMasukFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentB)
            .addToBackStack(null)
            .commit()
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
                val data = mapTransactionsToDaftarUangMasuk(transactionList)
                adapter = DaftarUangMasukAdapter(data)
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
            // Tambah header baru jika ada tanggal yang berbeda
            if (currentHeader?.date != transaction.date) {
                currentHeader?.let {
                    // Tambahkan footer untuk tanggal yang sama
                    data.add(Footer("Rp ${currentTotal}"))
                }
                currentHeader = Header(transaction.date, currentTotal)
                currentHeader?.let {
                    data.add(it)
                }
                currentTotal = 0 // Reset total
            }

            // Map transaction menjadi item
            val item = Item(
                time = transaction.time,
                to = transaction.to,
                from = transaction.from,
                description = transaction.description,
                amount = transaction.amount
            )
            data.add(item)

            // Update total berdasarkan tanggal sekarang
            currentTotal += transaction.amount
        }

        // Tambahkan footer untuk tanggal terakhir
        currentHeader?.let {
            data.add(Footer(formatRupiah(currentTotal)))
        }

        return data
    }

    private fun mapTransactionsToDaftarUangMasuk(transactions: List<Transaction>): List<Any> {
        val data = mutableListOf<Any>()
        var currentHeader: Header? = null
        var currentTotal = 0

        transactions.forEach { transaction ->
            // Tambah header baru jika ada tanggal yang berbeda
            if (currentHeader?.date != transaction.date) {
                // Tambahkan header baru
                currentHeader = Header(transaction.date, currentTotal)
                data.add(currentHeader!!)
                currentTotal = 0 // Reset total untuk tanggal baru
            }

            // Map transaction menjadi item
            val item = Item(
                time = transaction.time,
                to = transaction.to,
                from = transaction.from,
                description = transaction.description,
                amount = transaction.amount
            )
            data.add(item)

            // Update total berdasarkan tanggal sekarang
            currentTotal += transaction.amount
            currentHeader?.amount = currentTotal // Update total pada header
        }

        return data
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}