package com.notsatria.posapp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.notsatria.posapp.R
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.databinding.FragmentDaftarUangMasukBinding
import com.notsatria.posapp.models.Footer
import com.notsatria.posapp.models.Header
import com.notsatria.posapp.models.Item
import com.notsatria.posapp.ui.adapter.DaftarUangMasukAdapter
import com.notsatria.posapp.ui.adapter.TableAdapter
import com.notsatria.posapp.ui.inputuangmasuk.InputUangMasukFragment
import com.notsatria.posapp.utils.ViewModelFactory
import com.notsatria.posapp.utils.formatRupiah


class DaftarUangMasukFragment : Fragment() {
    private var _binding: FragmentDaftarUangMasukBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DaftarUangMasukViewModel> {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarUangMasukBinding.inflate(inflater, container, false)


        viewModel.getAllTransactions().observe(viewLifecycleOwner) { it ->
            if (it.isEmpty()) {
                binding.tvEmpty!!.visibility = View.VISIBLE
                binding.tvEmpty!!.text = "Data Kosong"
            } else {
                binding.tvEmpty!!.visibility = View.GONE
                showUangMasukRvPortrait(it)
            }
        }

        binding.btnBuatTransaksi!!.setOnClickListener {
            goToFragmentInputUangMasuk()
        }

        return binding.root
    }

    private fun goToFragmentInputUangMasuk() {
        val fragment = InputUangMasukFragment()
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment,
                InputUangMasukFragment::class.java.simpleName
            )
            .addToBackStack(null)
            .commit()
    }

    private fun showUangMasukRvPortrait(transactionList: List<TransactionEntity>) {
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

    private fun mapTransactionsToTableData(transactions: List<TransactionEntity>): List<Any> {
        val data = mutableListOf<Any>()
        var currentHeader: Header? = null
        var currentTotal = 0

        transactions.forEach { transaction ->
            // Tambah header baru jika ada tanggal yang berbeda
            if (currentHeader?.date != transaction.date.toString()) {
                currentHeader?.let {
                    // Tambahkan footer untuk tanggal yang sama
                    data.add(Footer("Rp ${currentTotal}"))
                }
                currentHeader = Header(transaction.date.toString(), currentTotal)
                currentHeader?.let {
                    data.add(it)
                }
                currentTotal = 0 // Reset total
            }

            // Map transaction menjadi item
            val item = Item(
                time = transaction.time!!,
                to = transaction.to!!,
                from = transaction.from!!,
                description = transaction.description!!,
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

    private fun mapTransactionsToDaftarUangMasuk(transactions: List<TransactionEntity>): List<Any> {
        val data = mutableListOf<Any>()
        var currentHeader: Header? = null
        var currentTotal = 0

        transactions.forEach { transaction ->
            // Tambah header baru jika ada tanggal yang berbeda
            if (currentHeader?.date != transaction.date.toString()) {
                // Tambahkan header baru
                currentHeader = Header(transaction.date.toString(), currentTotal)
                data.add(currentHeader!!)
                currentTotal = 0 // Reset total untuk tanggal baru
            }

            // Map transaction menjadi item
            val item = Item(
                time = transaction.time!!,
                to = transaction.to!!,
                from = transaction.from!!,
                description = transaction.description!!,
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