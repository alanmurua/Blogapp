package com.example.blogapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.blogapp.R
import com.example.blogapp.data.remote.home.HomeScreenDataSource
import com.example.blogapp.databinding.FragmentHomeScreenBinding
import com.example.blogapp.domain.home.HomeScreenRepoImpl
import com.example.blogapp.presentation.HomeScreenViewModel
import com.example.blogapp.presentation.HomeScreenViewModelFactory
import com.example.blogapp.ui.home.adapter.HomeScreenAdapter
import com.example.blogapp.core.Result
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.data.model.Post
import com.example.blogapp.ui.home.adapter.onPostClickListener


class HomeScreenFragment : Fragment(R.layout.fragment_home_screen), onPostClickListener {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewmodel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(
            HomeScreenRepoImpl(
                HomeScreenDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHomeScreenBinding.bind(view)


        viewmodel.fetchLatestPosts().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.rlProgressBar.show()

                }
                is Result.Success -> {

                    binding.rlProgressBar.hide()

                    if(result.data.isEmpty()){
                        binding.rlEmptyContainer.show()
                        return@Observer
                    } else {
                        binding.rlEmptyContainer.hide()
                    }
                    binding.rvHome.adapter = HomeScreenAdapter(result.data,this)


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

    override fun onLikeButtonClick(post: Post, liked: Boolean) {
        viewmodel.registerLikeButtonState(post.id,liked).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {

                }
                is Result.Success -> {

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