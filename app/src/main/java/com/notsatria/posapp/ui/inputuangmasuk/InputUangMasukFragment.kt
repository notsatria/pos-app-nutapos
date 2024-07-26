package com.notsatria.posapp.ui.inputuangmasuk

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notsatria.posapp.R
import com.notsatria.posapp.databinding.DialogImageSourceBinding
import com.notsatria.posapp.databinding.FragmentInputUangMasukBinding
import com.notsatria.posapp.utils.ViewModelFactory
import com.notsatria.posapp.utils.getCurrentTime
import com.notsatria.posapp.utils.getImageUri
import java.util.Date

class InputUangMasukFragment : Fragment() {

    private var _binding: FragmentInputUangMasukBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<InputUangMasukViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputUangMasukBinding.inflate(inflater, container, false)
        setupToolbar()
        setupEditText()
        setupJenisClickListener()
        setupIvPhoto()
        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            inflateMenu(R.menu.menu_simpan)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_suffix -> handleSave()
                    else -> false
                }
            }
        }
    }

    private fun setupEditText() {
        binding.apply {
            etDari.doOnTextChanged { text, _, _, _ -> validateEditText(etDari, text) }
            etMasukKe.doOnTextChanged { text, _, _, _ -> validateEditText(etMasukKe, text) }
            etJumlah.doOnTextChanged { text, _, _, _ -> validateEditText(etJumlah, text) }
            etKeterangan.doOnTextChanged { text, _, _, _ -> validateEditText(etKeterangan, text) }
        }
    }

    private fun validateEditText(editText: TextView, text: CharSequence?) {
        editText.error =
            if (text.isNullOrEmpty()) getString(R.string.field_couldn_be_empty) else null
    }

    private fun setupJenisClickListener() {
        binding.etJenis.setOnClickListener {
            showJenisDialog()
        }
    }

    private fun handleSave(): Boolean {
        val dari = binding.etDari.text.toString()
        val masukKe = binding.etMasukKe.text.toString()
        val jumlah = binding.etJumlah.text.toString()
        val keterangan = binding.etKeterangan.text.toString()
        val jenis = binding.etJenis.text.toString()
        val currentDate = Date()

        return if (dari.isNotEmpty() && masukKe.isNotEmpty() && jumlah.isNotEmpty() && keterangan.isNotEmpty() && jenis.isNotEmpty()) {
            try {
                viewModel.insertTransaction(
                    time = getCurrentTime(),
                    to = masukKe,
                    from = dari,
                    amount = jumlah.toInt(),
                    description = keterangan,
                    type = jenis,
                    date = currentDate.time
                )
                Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT)
                    .show()
                parentFragmentManager.popBackStack()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    requireContext(),
                    "Jumlah harus berupa angka yang valid",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        } else {
            Toast.makeText(requireContext(), R.string.field_couldn_be_empty, Toast.LENGTH_SHORT)
                .show()
            false
        }
    }

    private fun showJenisDialog() {
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

    private fun setupEditAndDeletePhoto() {
        if (currentImageUri != null) {
            binding.apply {
                ivPhoto.setOnClickListener {
                    return@setOnClickListener
                }
                btnChangeImage.visibility = View.VISIBLE
                btnDeleteImage.visibility = View.VISIBLE

                btnChangeImage.setOnClickListener {
                    showImageSourceDialog()
                }

                btnDeleteImage.setOnClickListener {
                    currentImageUri = null
                    ivPhoto.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_photo
                        )
                    )
                }
            }
        }
    }

    private fun setupIvPhoto() {
        binding.ivPhoto.setOnClickListener {
            showImageSourceDialog()
        }
    }

    private fun showImageSourceDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_image_source, null)
        val camera = dialogView.findViewById<LinearLayout>(R.id.btnCamera)
        val gallery = dialogView.findViewById<LinearLayout>(R.id.btnGallery)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        camera.setOnClickListener {
            startCamera()
            dialog.dismiss()
        }

        gallery.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else
            Toast.makeText(
                requireContext(),
                getString(R.string.image_not_avaliable), Toast.LENGTH_SHORT
            )
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri.let {
            binding.ivPhoto.setImageURI(it)
        }

        setupEditAndDeletePhoto()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
