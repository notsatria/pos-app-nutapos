package com.notsatria.posapp.ui.inputuangmasuk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notsatria.posapp.R
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.databinding.FragmentInputUangMasukBinding
import com.notsatria.posapp.utils.ViewModelFactory
import com.notsatria.posapp.utils.getCurrentTime
import java.util.Date

class EditUangMasukFragment : Fragment() {
    private var _binding: FragmentInputUangMasukBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<InputUangMasukViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputUangMasukBinding.inflate(inflater, container, false)

        with(binding) {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }


            // Retrieve data from arguments
            val args = arguments
            val id = args!!.getInt("id", -1)

            println("Args: $args")

            if (id != -1) {
                val to = args.getString("to", "")
                val from = args.getString("from", "")
                val description = args.getString("description", "")
                val amount = args.getInt("amount", 0)
                val type = args.getString("type", "")

                // Set data to EditText
                etMasukKe.setText(to)
                etDari.setText(from)
                etKeterangan.setText(description)
                etJumlah.setText(amount.toString())
                etJenis.setText(type)
            }

            toolbar.inflateMenu(R.menu.menu_simpan)

            setupEditText()

            toolbar.setOnMenuItemClickListener { item ->
                val dari = etDari.text.toString()
                val masukKe = etMasukKe.text.toString()
                val jumlah = etJumlah.text.toString()
                val keterangan = etKeterangan.text.toString()
                val jenis = etJenis.text.toString()
                val currentDate = Date()
                when (item.itemId) {
                    R.id.action_suffix -> {
                        if (dari.isNotEmpty() && masukKe.isNotEmpty() && jumlah.isNotEmpty() && keterangan.isNotEmpty() && jenis.isNotEmpty()) {
                            try {
                                val transaction = TransactionEntity(
                                    time = getCurrentTime(),
                                    to = masukKe,
                                    type = jenis,
                                    date = currentDate.time,
                                    description = keterangan,
                                    amount = jumlah.toInt(),
                                    from = dari,
                                )

                                println("Transaction: $transaction")
                                viewModel.updateTransaction(transaction)
                                Toast.makeText(
                                    requireContext(),
                                    "Data berhasil diperbarui",
                                    Toast.LENGTH_SHORT
                                ).show()
                                parentFragmentManager.popBackStack()

                            } catch (e: NumberFormatException) {
                                Toast.makeText(
                                    requireContext(),
                                    "Jumlah harus berupa angka yang valid",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            println("$dari, $jenis, $masukKe, $jumlah, $keterangan, $currentDate")
                            Toast.makeText(
                                requireContext(),
                                R.string.field_couldn_be_empty,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }

                    else -> false
                }
            }

            etJenis.setOnClickListener {
                showDialog()
            }

        }


        return binding.root
    }

    private fun setupEditText() {
        binding.apply {
            etDari.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    etDari.error = getString(R.string.field_couldn_be_empty)
                } else {
                    etDari.error = null
                }
            }

            etMasukKe.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    etMasukKe.error = getString(R.string.field_couldn_be_empty)
                } else {
                    etMasukKe.error = null
                }
            }

            etJumlah.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    etJumlah.error = getString(R.string.field_couldn_be_empty)
                } else {
                    etJumlah.error = null
                }
            }

            etKeterangan.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    etKeterangan.error = getString(R.string.field_couldn_be_empty)
                } else {
                    etKeterangan.error = null
                }
            }


        }
    }


    private fun showDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_jenis_pendapatan, null)
        val option1 = dialogView.findViewById<TextView>(R.id.tvOption1)
        val option2 = dialogView.findViewById<TextView>(R.id.tvOption2)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        option1.setOnClickListener {
            binding.etJenis.setText("Pendapatan Lain")
            dialog.dismiss()
        }

        option2.setOnClickListener {
            binding.etJenis.setText("Non Pendapatan")
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}