package com.example.blogapp.ui.camera

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.core.Result
import com.example.blogapp.data.remote.camera.CameraDataSource
import com.example.blogapp.databinding.FragmentCameraBinding
import com.example.blogapp.domain.camera.CameraRepoImpl
import com.example.blogapp.presentation.camera.CameraViewModel
import com.example.blogapp.presentation.camera.CameraViewModelFactory


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var binding: FragmentCameraBinding

    private val viewModel by viewModels<CameraViewModel> {
        CameraViewModelFactory(
            CameraRepoImpl(
                CameraDataSource()
            )
        )
    }
    private var bitmap: Bitmap? = null

    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.ivAddPhoto.setImageBitmap(imageBitmap)
                bitmap = imageBitmap
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            resultLauncher.launch(takePictureIntent)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "No se encontro ninguna app para abrir la camara",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnAddPhoto.setOnClickListener {
            bitmap?.let {
                viewModel.uploadPhoto(it, binding.etAddDescription.text.toString().trim())
                    .observe(viewLifecycleOwner,
                        Observer { result ->
                            when (result) {
                                is Result.Loading -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Uploading photo... ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is Result.Success -> {
                                    findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
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