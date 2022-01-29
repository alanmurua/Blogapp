package com.example.blogapp.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.core.Result
import com.example.blogapp.data.remote.auth.AuthDataSource
import com.example.blogapp.databinding.FragmentSetupProfileBinding
import com.example.blogapp.domain.auth.AuthRepoImpl
import com.example.blogapp.presentation.auth.AuthViewModel
import com.example.blogapp.presentation.auth.AuthViewModelFactory


class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding
    private var bitmap: Bitmap? = null

    private val viewmodel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.ivProfileImage.setImageBitmap(imageBitmap)
                bitmap = imageBitmap
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)

        binding.ivProfileImage.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                resultLauncher.launch(takePicture)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "No se encontro ninguna app para abrir la camara",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnProfile.setOnClickListener {
            val username = binding.etUserName.text.toString().trim()
            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Uploading photo...").create()

            bitmap?.let { bitmap ->
                if (username.isNotEmpty()) {
                    viewmodel.updateUserProfile(bitmap, username).observe(viewLifecycleOwner,
                        Observer { result ->
                            when (result) {

                                is Result.Loading -> {
                                    alertDialog.show()
                                }

                                is Result.Success -> {
                                    alertDialog.dismiss()
                                    findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                                }

                                is Result.Failure -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Ocurrio un error: ${result.exception}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                }
            }
        }
    }
}