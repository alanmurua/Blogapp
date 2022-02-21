package com.example.blogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.btnNavigation.setupWithNavController(navController)

        observeDestinationChange()

    }

    private fun observeDestinationChange() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {

                R.id.loginFragment -> {
                    binding.btnNavigation.hide()
                }

                R.id.registerFragment -> {
                    binding.btnNavigation.hide()
                }


                else -> {
                    binding.btnNavigation.show()
                }
            }
        }
    }
}