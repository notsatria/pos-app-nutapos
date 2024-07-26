package com.notsatria.posapp.ui.main

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notsatria.posapp.R
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.databinding.FragmentDaftarUangMasukBinding
import com.notsatria.posapp.models.Footer
import com.notsatria.posapp.models.Header
import com.notsatria.posapp.models.Item
import com.notsatria.posapp.ui.adapter.DaftarUangMasukAdapter
import com.notsatria.posapp.ui.adapter.TableAdapter
import com.notsatria.posapp.ui.edituangmasuk.EditUangMasukFragment
import com.notsatria.posapp.ui.inputuangmasuk.InputUangMasukFragment
import com.notsatria.posapp.utils.ViewModelFactory
import com.notsatria.posapp.utils.convertTimestampToString
import com.notsatria.posapp.utils.formatRupiah
import com.notsatria.posapp.utils.getEndOfDayInMillis
import com.notsatria.posapp.utils.getStartOfDayInMillis
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class DaftarUangMasukFragment : Fragment() {
    private var _binding: FragmentDaftarUangMasukBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DaftarUangMasukViewModel> {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    private var startDate: Long? = null
    private var endDate: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarUangMasukBinding.inflate(inflater, container, false)


        viewModel.getAllTransactions().observe(viewLifecycleOwner) { it ->
            if (it.isEmpty()) {
                binding.rvDaftarUangMasuk.visibility = View.GONE
                binding.tvEmpty!!.visibility = View.VISIBLE
                binding.tvEmpty!!.text = "Data Kosong"
            } else {
                println("Data: $it")
                binding.rvDaftarUangMasuk.visibility = View.VISIBLE
                binding.tvEmpty!!.visibility = View.GONE
                showUangMasukRvPortrait(it)
            }
        }

        binding.btnBuatTransaksi?.setOnClickListener {
            goToFragmentInputUangMasuk()
        }

        binding.btnDatePicker?.setOnClickListener {
            showDateRangePicker()
        }

        return binding.root
    }

    private fun showDateRangePicker() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2010)
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val minDate = calendar.timeInMillis

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.from(minDate))
                    .build()
            )
            .build()

        dateRangePicker.show(parentFragmentManager, "DateRangePicker")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            startDate = getStartOfDayInMillis(selection.first)
            endDate = getEndOfDayInMillis(selection.second)

            val startDateString = convertTimestampToString(startDate!!)
            val endDateString = convertTimestampToString(endDate!!)

            binding.tvDateRange.text = "$startDateString - $endDateString"

            filterTransactions(startDate!!, endDate!!)
        }
    }

    private fun filterTransactions(startDate: Long, endDate: Long) {
        println("Date range: $startDate - $endDate")
        viewModel.getTransactionsByDateRange(startDate, endDate)
            .observe(viewLifecycleOwner) { transactions ->
                if (transactions.isEmpty()) {
                    binding.rvDaftarUangMasuk.visibility = View.GONE
                    binding.tvEmpty!!.visibility = View.VISIBLE
                    binding.tvEmpty!!.text = "Data Kosong"
                } else {
                    binding.rvDaftarUangMasuk.visibility = View.VISIBLE
                    binding.tvEmpty!!.visibility = View.GONE
                    showUangMasukRvPortrait(transactions)
                }
            }
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

    private fun goToFragmentInputUangMasukWithData(item: Item) {
        val fragment = EditUangMasukFragment().apply {
            arguments = Bundle().apply {
                putInt("id", item.id)
                putString("time", item.time)
                putString("to", item.to)
                putString("from", item.from)
                putString("description", item.description)
                putInt("amount", item.amount)
                putString("type", item.type)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment,
                EditUangMasukFragment::class.java.simpleName
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
                val rvAdapter = DaftarUangMasukAdapter(
                    data,
                    onDeleteClickListener = { itemId ->
                        viewModel.deleteTransaction(itemId)
                    },
                    onEditClickListener = { item ->
                        goToFragmentInputUangMasukWithData(item)
                    },
                    onLihatClickListener = { item ->
                        showImageDialog(item.imageUri!!)
                    },
                )

                adapter = rvAdapter
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val data = mapTransactionsToTableData(transactionList)
                adapter = TableAdapter(data)
            }
        }
    }

    private fun showImageDialog(imageUri: String) {
        println("Image URI: $imageUri")
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_image, null)
        val ivPhoto = dialogView.findViewById<ImageView>(R.id.ivImage)
        val resolver = requireContext().contentResolver
        val uri = Uri.parse(imageUri)

        resolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        val bitmap = resolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        ivPhoto.setImageBitmap(bitmap)

        dialog.show()
    }


    private fun mapTransactionsToTableData(transactions: List<TransactionEntity>): List<Any> {
        val data = mutableListOf<Any>()
        var currentHeader: Header? = null
        var currentTotal = 0

        transactions.forEach { transaction ->
            // Tambah header baru jika ada tanggal yang berbeda
            if (currentHeader?.date != convertTimestampToString(transaction.date!!)) {
                currentHeader?.let {
                    // Tambahkan footer untuk tanggal yang sama
                    data.add(Footer("Rp ${currentTotal}"))
                }
                currentHeader = Header(convertTimestampToString(transaction.date!!), currentTotal)
                currentHeader?.let {
                    data.add(it)
                }
                currentTotal = 0 // Reset total
            }

            // Map transaction menjadi item
            val item = Item(
                id = transaction.id,
                time = transaction.time!!,
                to = transaction.to!!,
                from = transaction.from!!,
                description = transaction.description!!,
                amount = transaction.amount,
                type = transaction.type!!,
                imageUri = transaction.imageUri,
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
            if (currentHeader?.date != convertTimestampToString(transaction.date!!)) {
                // Tambahkan header baru
                currentHeader = Header(convertTimestampToString(transaction.date!!), currentTotal)
                data.add(currentHeader!!)
                currentTotal = 0 // Reset total untuk tanggal baru
            }

            // Map transaction menjadi item
            val item = Item(
                id = transaction.id,
                time = transaction.time!!,
                to = transaction.to!!,
                from = transaction.from!!,
                description = transaction.description!!,
                amount = transaction.amount,
                type = transaction.type!!,
                imageUri = transaction.imageUri,
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