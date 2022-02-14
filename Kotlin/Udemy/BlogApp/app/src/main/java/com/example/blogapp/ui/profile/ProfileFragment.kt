package com.example.blogapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.blogapp.R
import com.example.blogapp.core.Result
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.data.remote.profile.ProfileDataSource
import com.example.blogapp.databinding.FragmentProfileBinding
import com.example.blogapp.domain.profile.ProfileRepoImpl
import com.example.blogapp.presentation.profile.ProfileViewModel
import com.example.blogapp.presentation.profile.ProfileViewModelFactory
import com.example.blogapp.ui.profile.adapter.ProfileAdapter


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    private val viewmodel by viewModels<ProfileViewModel>{ ProfileViewModelFactory(ProfileRepoImpl(
        ProfileDataSource()
    )) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        viewmodel.getUserInfo().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {
                    binding.rlProgressBar.show()
                }
                is Result.Success -> {

                    binding.rlProgressBar.hide()
                    result.data?.let {
                        Glide.with(this).load(result.data.photoUrl).into(binding.ciProfilePicture)
                        binding.tvNombre.text = result.data.displayName
                        binding.tvEmail.text = result.data.email
                    }

                }
                is Result.Failure -> {
                    binding.rlProgressBar.hide()
                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


        viewmodel.getUserPosts().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {
                    binding.rlProgressBar.show()

                }
                is Result.Success -> {
                    if(result.data.isEmpty()){
                        binding.rlEmptyContainer.show()
                        return@Observer
                    } else {
                        binding.rlEmptyContainer.hide()
                    }
                    binding.rvHome.adapter = ProfileAdapter(result.data)
                }
                is Result.Failure -> {
                    binding.rlProgressBar.hide()
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