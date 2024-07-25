package com.notsatria.posapp.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notsatria.posapp.R
import com.notsatria.posapp.databinding.FragmentDaftarUangMasukBinding
import com.notsatria.posapp.databinding.FragmentInputUangMasukBinding

class InputUangMasukFragment : Fragment() {
    private var _binding: FragmentInputUangMasukBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            toolbar.inflateMenu(R.menu.menu_simpan)

            setupEditText()

            val dari = etDari.text.toString()
            val masukKe = etMasukKe.text.toString()
            val jumlah = etJumlah.text.toString()
            val keterangan = etKeterangan.text.toString()
            val jenis = etJenis.text.toString()

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_suffix -> {
                        if (dari.isNotEmpty() || masukKe.isNotEmpty() || jumlah.isNotEmpty() || keterangan.isNotEmpty() || jenis.isNotEmpty()) {
                            saveTransaction()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                R.string.field_couldn_be_empty,
                                Toast.LENGTH_SHORT
                            )
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